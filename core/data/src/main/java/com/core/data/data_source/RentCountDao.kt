package com.core.data.data_source

import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.MapInfo
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.core.common.util.SimpleItem
import com.core.data.data_source.util.FLAT_CATEGORY
import com.core.data.data_source.util.SECTION_CATEGORY
import com.feature_home.domain.model.AdditionalInfo
import com.feature_home.domain.model.FinCategory
import com.feature_home.domain.model.FinResultsFlat
import com.feature_home.domain.model.FinResultsSection
import com.feature_home.domain.model.FlatFinInfo
import com.feature_home.domain.model.FullGuestInfo
import com.feature_home.domain.model.RentDates
import com.feature_home.domain.model.SectionInfo
import com.feature_home.domain.model.TransactionInfo
import com.feature_transactions.domain.model.CategoriesFilter
import com.feature_transactions.domain.model.TransactionListItem
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth

@Dao
interface RentCountDao {

    //DATABASE INITIALIZING
    @Transaction
    suspend fun addBasic(
        name: String,
        blockCategory: String,
        incomeCategories: List<FinCategory>?,
        expCategories: List<FinCategory>?,
    ){
        val blockId = addNewBlock(
            Blocks(
                blockId = null,
                blockCategory = blockCategory,
                name = name
            )
        ).toInt()

        incomeCategories?.forEach { category->
            addNewCategory(
                Categories(
                    categoryId = null,
                    blockId = blockId,
                    standardCategoryId = category.standard_category_id,
                    isIncome = true,
                    name = category.name
                )
            )
        }
        expCategories?.forEach { category->
            addNewCategory(
                Categories(
                    categoryId = null,
                    blockId = blockId,
                    standardCategoryId = category.standard_category_id,
                    isIncome = false,
                    name = category.name
                )
            )
        }
    }

    //BLOCKS
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewBlock(block: Blocks): Long

    @Query("UPDATE blocks SET name =:name WHERE block_id=:block_id")
    suspend fun updateBlockName(block_id: Int, name: String)

    @Query("SELECT name FROM blocks WHERE block_id=:block_id")
    suspend fun getBlockNameById(block_id: Int):String

    @Query("SELECT * FROM blocks WHERE block_category=:category")
    suspend fun getBlocksByCat(category: String):List<Blocks>

    @Query("SELECT block_id AS id, name FROM blocks WHERE block_category=:category")
    suspend fun getBlocksSIByCat(category: String):List<SimpleItem>

    @Query("SELECT block_id AS id, name FROM blocks")
    suspend fun getBlocks():List<SimpleItem>

    //TRANSACTIONS
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewTransaction(transaction: Transactions):Long

    @Query("DELETE FROM transactions WHERE transaction_id=:transaction_id")
    suspend fun deleteTransactionById(transaction_id: Int)

    @Query("SELECT sum(amount) FROM transactions")
    fun getSumOfAllTransactions(): Flow<Int?>

    @Query("WITH flatIds AS (SELECT block_id FROM blocks WHERE block_category=:category) " +
            "SELECT sum(CASE WHEN amount>0 THEN amount ELSE 0 END) AS income, sum(CASE WHEN amount<0 THEN amount ELSE 0 END) AS expenses " +
            "FROM transactions WHERE block_id IN flatIds AND year=:year AND month=:month")
    suspend fun getIncomeExpenses(category: String, year: Int, month: Int): IncomeExpenses

    @Query("WITH sectionsIds AS (SELECT block_id FROM blocks WHERE block_category=:category) " +
            "SELECT block_id as id, sum(CASE WHEN year=:year AND month=:month THEN amount ELSE 0 END) AS amount " +
            "FROM transactions WHERE block_id IN sectionsIds GROUP BY block_id")
    suspend fun finResultsSections(category: String, year: Int, month: Int):List<FinResultsSection>

    @Query("WITH sectionsIds AS (SELECT block_id as id FROM blocks WHERE block_category=:category), " +
            "ft AS (SELECT block_id as id, sum(amount) AS amount FROM transactions WHERE block_id IN sectionsIds AND year=:year AND month=:month GROUP BY block_id) " +
            "SELECT sectionsIds.id as id, ft.amount as amount " +
            "FROM sectionsIds JOIN ft ON sectionsIds.id=ft.id")
    fun finResultsSectionsFlow(category: String, year: Int, month: Int): Flow<List<FinResultsSection>>


    //Categories
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewCategory(category: Categories)

    @Query("SELECT category_id as id, standard_category_id, name FROM categories WHERE block_id=:block_id")
    suspend fun getCatById(block_id: Int):List<FinCategory>

    @Query("SELECT category_id as id, standard_category_id, name FROM categories WHERE block_id=:block_id AND is_income=:is_income")
    suspend fun getExpCatById(block_id: Int, is_income: Boolean=false):List<FinCategory>

    @Query("SELECT category_id FROM categories WHERE block_id=:block_id AND is_income=:is_income")
    suspend fun getCategoryFlat(block_id: Int, is_income: Boolean=true): Int


    //RENT
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewRent(rents: Rents): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRentTrack(rentsTrack: RentsTrack)

    @Query("DELETE FROM rents_track WHERE rent_id=:rent_id")
    suspend fun deleteRentTrackById(rent_id: Int)

    @Query("SELECT transaction_id FROM rents_track WHERE rent_id=:rent_id")
    suspend fun getTransactionsId(rent_id: Int): List<Int>

    @Query("SELECT * FROM rents_track WHERE rent_id=:rent_id")
    suspend fun getRentsTrackByRentId(rent_id: Int): List<RentsTrack>

    @Transaction
    suspend fun addNewRentInfo(rents: Rents, currency_name: String, listOfTracks: List<RentsTrack>, currentDate: Long){
        val categoryId = getCategoryFlat(block_id = rents.blockId)
        val rentId = addNewRent(rents=rents).toInt()
        if(rents.isPaid){
            listOfTracks.forEach { track ->
                val transactionId = addNewTransaction(
                    Transactions(
                        transactionId = null,
                        blockId = rents.blockId,
                        categoryId = categoryId,
                        amount = track.amount,
                        currencyName = currency_name,
                        year = track.year,
                        month = track.month,
                        currentDate = currentDate,
                        comment = "${rents.name} - ${track.nights}N - ${track.month}/${track.year}"
                    )
                ).toInt()
                addRentTrack(track.copy(
                    transaction_id = transactionId,
                    rentId = rentId
                ))
            }
        } else {
            listOfTracks.forEach { track ->
                addRentTrack(track.copy(rentId = rentId))
            }
        }
    }

    @Transaction
    suspend fun addNewGuest(rents: Rents, currency_name: String, currentDate: Long,  month: Int, year: Int){
        val categoryId =  if(rents.isPaid) getCategoryFlat(block_id = rents.blockId) else 0
        Log.d("New Guest", "Category Id: $categoryId")
        val rentId = addNewRent(rents=rents).toInt()
        Log.d("New Guest", "RentId: $rentId")
        val transactionId = if(rents.isPaid) addNewTransaction(
            Transactions(
                transactionId = null,
                blockId = rents.blockId,
                categoryId = categoryId,
                amount = rents.forAllNights,
                currencyName = currency_name,
                year = year,
                month = month,
                currentDate = currentDate,
                comment = "${rents.name} - ${rents.nights}N - ${month}/${year}"
            )
        ).toInt() else null
        Log.d("New Guest", "transactionId: $transactionId")
        addRentTrack(
            rentsTrack = RentsTrack(
                trackId = null,
                rentId = rentId,
                year = year,
                month = month,
                nights = rents.nights,
                amount = rents.forAllNights,
                isPaid = rents.isPaid,
                transaction_id = transactionId
            ))
        Log.d("New Guest", "RentsTrack successfully No way")
    }

    @Transaction
    suspend fun renewRentDates(rents: Rents, currency_name: String, listOfTracks: List<RentsTrack>, currentDate: Long){
        val transactionsIds = getTransactionsId(rents.rentId!!)
        if(transactionsIds.isNotEmpty()){
            transactionsIds.forEach { id->
                deleteTransactionById(transaction_id = id)
            }
        }
        deleteRentTrackById(rent_id = rents.rentId)
        addNewRentInfo(
            rents = rents,
            currency_name = currency_name,
            listOfTracks=listOfTracks,
            currentDate=currentDate
        )
    }

    @Transaction
    suspend fun renewRentInfo(rents: Rents, isAmountChanged:Boolean, isStatusChanged: Boolean, currency_name: String, currentDate: Long){
        addNewRent(rents=rents)
        if(isAmountChanged || isStatusChanged){
            val rentsTrack = getRentsTrackByRentId(rent_id = rents.rentId!!)
            var newRentsTrack = rentsTrack
            if(isAmountChanged){
                val allNights = rents.nights
                var lastDays = allNights
                var lastAmount = rents.forAllNights
                newRentsTrack = newRentsTrack.map { rentTrack ->
                    val amount = if(rentTrack.nights==allNights) rents.forAllNights else
                        if(lastDays==rentTrack.nights) lastAmount else rentTrack.nights * rents.forNight
                    lastDays -= rentTrack.nights
                    lastAmount -= (rentTrack.nights * rents.forNight)
                    rentTrack.copy(
                        amount = amount
                    )
                }
            }
            if(isStatusChanged){
                val categoryId = getCategoryFlat(block_id = rents.blockId)

                if(rents.isPaid){
                    newRentsTrack = newRentsTrack.map {rentTrack ->
                        val transactionsId = addNewTransaction(
                            transaction = Transactions(
                                transactionId = null,
                                blockId = rents.blockId,
                                categoryId = categoryId,
                                amount = rentTrack.amount,
                                currencyName = currency_name,
                                year = rentTrack.year,
                                month = rentTrack.month,
                                currentDate=currentDate,
                                comment = "${rents.name} - ${rentTrack.nights}N - ${rentTrack.month}/${rentTrack.year}"
                            )
                        ).toInt()
                        rentTrack.copy(
                            isPaid = rents.isPaid,
                            transaction_id = transactionsId
                        )
                    }
                } else {
                    newRentsTrack = newRentsTrack.map {rentTrack ->
                        if(rentTrack.transaction_id!=null){
                            deleteTransactionById(transaction_id = rentTrack.transaction_id)
                        }
                        rentTrack.copy(
                            isPaid = false,
                            transaction_id = null
                        )
                    }
                }
            }

            newRentsTrack.forEach { rentTrack ->
                addRentTrack(rentTrack)
            }
        }

    }

    @Query("UPDATE rents_track SET transaction_id =:nullValue WHERE rent_id=:rent_id")
    suspend fun deleteTransactionsIdInTrack(nullValue: Int?=null, rent_id: Int)

    @Query("UPDATE rents_track SET transaction_id =:transaction_id WHERE track_id=:track_id")
    suspend fun updateRentTrackTransactionId(transaction_id: Int, track_id: Int)

    @Query("UPDATE rents SET is_paid=:status WHERE rent_id=:rent_id")
    suspend fun updateRentStatus(status: Boolean, rent_id: Int)

    @Query("SELECT sum(nights) FROM rents_track WHERE  year=:year AND month=:month")
    suspend fun getRentDays(year: Int, month:Int): Int?


    @Query("WITH RECURSIVE dates AS " +
            "(SELECT rent_id, start_date, end_date " +
            "FROM rents WHERE block_id=:flatId " +
            "UNION ALL " +
            "SELECT rent_id, (CAST(strftime('%s', datetime(start_date/1000, 'unixepoch', '+1 days')) AS INTEGER) * 1000)  AS start_date, end_date " +
            "FROM dates " +
            "WHERE (CAST(strftime('%s', datetime(start_date/1000, 'unixepoch', '+1 days')) AS INTEGER) * 1000) BETWEEN start_date AND end_date ), " +
            "vacantDays AS " +
            "(SELECT v.date as date FROM " +
            "(SELECT start_date as date FROM rents WHERE block_id=:flatId " +
            "UNION ALL " +
            "SELECT end_date as date FROM rents WHERE block_id=:flatId " +
            ") as v  GROUP BY date HAVING COUNT(date) = 1)" +
            "SELECT DISTINCT start_date FROM dates" +
            " WHERE start_date not in (SELECT date FROM vacantDays) ORDER BY start_date ")
    suspend fun getAllRentDates(flatId: Int): List<Long>

    @Query("SELECT sum(amount) FROM rents_track WHERE  year=:year AND month=:month AND transaction_id is not null")
    suspend fun getPaidSumRent(year: Int, month:Int): Int?

    @Query("SELECT sum(amount) FROM rents_track WHERE  year=:year AND month=:month AND transaction_id is null")
    suspend fun getUnpaidSumRent(year: Int, month:Int): Int?


    @Query("SELECT * FROM rents_track WHERE rent_id=:rent_id")
    suspend fun getTracks(rent_id: Int): List<RentsTrack>

    @Query("SELECT * FROM rents WHERE rent_id=:rent_id")
    suspend fun getRentBy(rent_id: Int): Rents

    @Query("SELECT * FROM rents WHERE block_id=:block_id")
    suspend fun getRentById(block_id: Int): Rents

    @Query("WITH rentsTrack AS (SELECT rent_id FROM rents_track WHERE year=:year AND month=:month) " +
            "SELECT rent_id as id, start_date, end_date, name, phone, comment, for_night, for_all_nights, is_paid FROM rents " +
            "WHERE block_id=:block_id AND rent_id IN rentsTrack")
    fun getAllRentsMonth(block_id: Int, year: Int, month: Int): Flow<List<FullGuestInfo>>

    @MapInfo(keyColumn = "block_id")
    @Query("WITH filteredTransactions AS (SELECT * FROM transactions WHERE block_id IN (:blocksId) AND year=:year AND month=:month) " +
            "SELECT filteredTransactions.block_id AS block_id, filteredTransactions.transaction_id As id, categories.is_income AS isIncome, " +
            "categories.category_id AS categoryId, filteredTransactions.amount AS amount " +
            "FROM filteredTransactions JOIN categories ON filteredTransactions.category_id=categories.category_id")
    suspend fun getSectionTransactionsBYMonth(year: Int, month: Int, blocksId: List<Int>): Map<Int, List<TransactionInfo>>

    @Query("WITH filteredTransactions AS (SELECT * FROM transactions WHERE block_id=:block_id AND year=:year AND month=:month) " +
            "SELECT filteredTransactions.transaction_id As id, categories.is_income AS isIncome, " +
            "categories.category_id AS categoryId, filteredTransactions.amount AS amount " +
            "FROM filteredTransactions JOIN categories ON filteredTransactions.category_id=categories.category_id")
    fun getTransactionsByBlockIdMonth(year: Int, month: Int, block_id: Int): Flow<List<TransactionInfo>>

    @MapInfo(keyColumn = "block_id")
    @Query("SELECT block_id, category_id as id, standard_category_id as standard_category_id, name as name FROM categories WHERE is_income=:is_income")
    suspend fun getSpecificCategories(is_income: Boolean):Map<Int, List<FinCategory>>

    @MapInfo(keyColumn = "block_id")
    @Query("SELECT * FROM rents WHERE (start_date BETWEEN :start_date AND :end_date) OR (end_date BETWEEN :start_date AND :end_date)")
    suspend fun getGuests(start_date: Long, end_date: Long):Map<Int, List<Rents>>

    @Query("WITH flats AS (SELECT * FROM blocks WHERE block_category=:flatCategory), " +
            "flatsId AS (SELECT block_id FROM flats), " +
            "ft AS (SELECT block_id, SUM(amount) as amount FROM transactions WHERE block_id IN flatsId AND year=:year AND month=:month GROUP BY block_id)," +
            "fRents AS (SELECT * FROM rents WHERE block_id IN flatsId), " +
            "rentsId AS (SELECT rent_id FROM fRents), " +
            "rentTrack AS (SELECT rent_id, sum(nights) as nights FROM rents_track WHERE rent_id IN rentsId AND year=:year AND month=:month GROUP BY rent_id), " +
            "rentPercent AS (SELECT fRents.block_id, SUM(rentTrack.nights) AS nights FROM fRents JOIN rentTrack ON fRents.rent_id=rentTrack.rent_id GROUP BY block_id) " +
            "SELECT flats.block_id AS id, flats.name AS name, ft.amount AS current_month_amount, CAST(rentPercent.nights AS FLOAT)/:numDays AS rent_percent FROM flats " +
            "LEFT JOIN ft ON flats.block_id=ft.block_id " +
            "LEFT JOIN rentPercent ON flats.block_id=rentPercent.block_id")
    fun getListOfFlats(year: Int, month: Int, flatCategory:String, numDays: Int): Flow<List<FlatFinInfo>>

    @MapInfo(keyColumn = "block_id", valueColumn = "amount")
    @Query("SELECT block_id, SUM(amount) AS amount FROM transactions WHERE block_id IN (:blocksId) AND year=:year AND month=:month")
    suspend fun getTransactionByBlockList(year: Int, month: Int, blocksId: List<Int>):Map<Int, Int>

    @MapInfo(keyColumn = "block_id")
    @Query("SELECT rents.block_id AS block_id, rents.name AS rent_name, rents_track.nights AS nights, rents_track.is_paid AS is_paid " +
            "FROM rents_track JOIN rents ON rents_track.rent_id=rents.rent_id " +
            "WHERE rents_track.year=:year AND rents_track.month=:month ")
    fun getFlatsAdditionalInfo(year: Int, month: Int): Flow<Map<Int, List<AdditionalInfo>>>

    @Transaction
    suspend fun getFlatsInfo(year: Int, month: Int, start_date: Long, end_date: Long, flatCategory:String): Triple<List<Blocks>, Map<Int, List<Rents>>, Map<Int, Int>> {
        val blocks = getBlocksByCat(category = flatCategory)
        val blocksId = blocks.map{it.blockId} as List<Int>
        val guests = getGuests(start_date=start_date, end_date=end_date)
        val amountFlat = getTransactionByBlockList(year=year, month=month, blocksId=blocksId)
        return Triple(blocks, guests, amountFlat)
    }

    @Transaction
    suspend fun getFinResultFlat(
        yearMonth: YearMonth,
        numDays: Int,
        year: Int,
        month: Int
    ): FinResultsFlat {
        val incomeExpenses = getIncomeExpenses(
            category = SECTION_CATEGORY,
            year = year,
            month = month
        )
        val unpaid = getUnpaidSumRent(year = year, month = month)
        val days = getRentDays(year = year, month = month) ?: 0
        val rentPercent = if (days == 0) 0f else (days / numDays).toFloat()
        return FinResultsFlat(
            month = month,
            year = year,
            paid_amount = incomeExpenses.income,
            expenses_amount = incomeExpenses.expenses,
            unpaid_amount = unpaid ?: 0,
            rent_percent = rentPercent
        )
    }

    @Query("WITH flatPaid AS (SELECT year, month, sum(CASE WHEN amount>0 THEN amount ELSE 0 END) AS paid_amount, sum(CASE WHEN amount<0 THEN amount ELSE 0 END) AS expenses_amount " +
            "FROM transactions WHERE block_id=:flat_id AND year=:year GROUP BY year, month), " +
            "rentIds AS (SELECT rent_id FROM rents WHERE block_id=:flat_id), " +
            "flatRent AS (SELECT year, month, sum(amount) AS unpaid_amount, sum(nights) AS nights FROM rents_track WHERE rent_id IN rentIds AND year=:year AND is_paid=:isPaid GROUP BY year, month) " +
            "SELECT flatPaid.year AS year, flatPaid.month AS month, flatPaid.paid_amount AS paid_amount, flatRent.unpaid_amount AS unpaid_amount, flatPaid.expenses_amount AS expenses_amount, " +
            "CAST(flatRent.nights AS FLOAT)/(STRFTIME('%d', DATE(flatPaid.year ||'-01-01','+'||(flatPaid.month-1)||' month', 'start of month','+1 month', '-1 day'))) AS rent_percent " +
            "FROM flatPaid JOIN flatRent ON flatPaid.month=flatRent.month")
    fun getFinResultFlatMonthly(flat_id: Int, year: Int, isPaid:Boolean=false): Flow<List<FinResultsFlat>>

    @Query("WITH flatPaid AS (SELECT year, month, sum(CASE WHEN amount>0 THEN amount ELSE 0 END) AS paid_amount, sum(CASE WHEN amount<0 THEN amount ELSE 0 END) AS expenses_amount " +
            "FROM transactions WHERE month=:month AND year=:year GROUP BY year, month), " +
            "flatRent AS (SELECT year, month, sum(amount) AS unpaid_amount, sum(nights) AS nights FROM rents_track WHERE month=:month AND year=:year GROUP BY year, month) " +
            "SELECT flatPaid.year AS year, flatPaid.month AS month, flatPaid.paid_amount AS paid_amount, flatRent.unpaid_amount AS unpaid_amount, flatPaid.expenses_amount AS expenses_amount, " +
            ":rentPercent AS rent_percent " +
            "FROM flatPaid JOIN flatRent ON flatPaid.month=flatRent.month")
    fun getAllFinResultFlatByMonth(year: Int, month: Int, rentPercent: Float = 0f): Flow<FinResultsFlat?>

    @Transaction
    suspend fun updatePaidStatusGuest(rent_id: Int, currency_name: String, status: Boolean, currentDate: Long){
        val rent = getRentBy(rent_id=rent_id)
        val isPaid = rent.isPaid
        if(status!=isPaid){
            if(isPaid){
                val listOfTransactionsIds = getTransactionsId(rent_id=rent_id)
                if(listOfTransactionsIds.isNotEmpty()){
                    listOfTransactionsIds.forEach { transaction_id ->
                        deleteTransactionById(transaction_id=transaction_id)
                    }
                }
                deleteTransactionsIdInTrack(rent_id=rent_id)
            } else {
                val listOfTracks = getTracks(rent_id=rent_id)
                val categoryId = getCategoryFlat(rent.blockId)
                listOfTracks.forEach {track ->
                    val transactionId = addNewTransaction(
                        Transactions(
                            transactionId = null,
                            blockId = rent.blockId,
                            categoryId = categoryId,
                            amount = track.amount,
                            currencyName = currency_name,
                            year = track.year,
                            month = track.month,
                            currentDate=currentDate,
                            comment = "${rent.name} - ${track.nights}N - ${track.month}/${track.year}"
                        )
                    ).toInt()
                    updateRentTrackTransactionId(transaction_id = transactionId, track_id = track.trackId!!)
                }
            }
            updateRentStatus(status=status, rent_id = rent_id)
        }

    }


    @Transaction
    suspend fun getListSections(sectionCategory: String, year: Int, month: Int): List<SectionInfo>{
        val blocks = getBlocksByCat(category = sectionCategory)
        val blocksId = blocks.map{it.blockId} as List<Int>
        val transactionsInfo = getSectionTransactionsBYMonth(year=year, month=month, blocksId = blocksId)
        val incomeCategories = getSpecificCategories(is_income = true)
        val expensesCategories = getSpecificCategories(is_income = false)
        val listOfSections = mutableListOf<SectionInfo>()
        blocks.forEach { block ->
            val incomeCat = incomeCategories[block.blockId] ?: emptyList()
            val expCat = expensesCategories[block.blockId] ?: emptyList()
            val transactions = transactionsInfo[block.blockId] ?: emptyList()
            val newSelectedId = if(incomeCat.isNotEmpty()) incomeCat.first().id else 0
            listOfSections.add(
                SectionInfo(
                    id = block.blockId,
                    name = block.name,
                    incomeCategories =  incomeCat,
                    expensesCategories = expCat,
                    transactionsDisplay = transactions,
                    selectedCategoryId = newSelectedId
                )
            )
        }
        return listOfSections
    }

    @Transaction
    suspend fun editSection(
        blockId: Int,
        newName: String?,
        newIncomeCategories: List<FinCategory>?,
        newExpCategories: List<FinCategory>?,
        year: Int,
        month: Int
    ): List<SectionInfo>{
        if(newName!=null){
            updateBlockName(block_id=blockId, name=newName )
        }
        newIncomeCategories?.forEach { category->
            addNewCategory(
                Categories(
                    categoryId = null,
                    blockId = blockId,
                    standardCategoryId = category.standard_category_id,
                    isIncome = true,
                    name = category.name
                )
            )
        }
        newExpCategories?.forEach { category->
            addNewCategory(
                Categories(
                    categoryId = null,
                    blockId = blockId,
                    standardCategoryId = category.standard_category_id,
                    isIncome = false,
                    name = category.name
                )
            )
        }
        return getListSections(
            sectionCategory = SECTION_CATEGORY,
            year = year,
            month = month
        )
    }

    @Transaction
    suspend fun addTransaction(
        transaction: Transactions,
        year: Int,
        month: Int
    ): List<SectionInfo>{
        addNewTransaction(transaction=transaction)
        return getListSections(
            sectionCategory = SECTION_CATEGORY,
            year = year,
            month = month
        )
    }



    @Transaction
    suspend fun addNewSection(
        name: String,
        incomeCategories: List<FinCategory>?,
        expCategories: List<FinCategory>?,
        year: Int,
        month: Int
    ): List<SectionInfo>{
        val blockId = addNewBlock(
            Blocks(
                blockId = null,
                blockCategory = SECTION_CATEGORY,
                name = name
            )
        ).toInt()

        incomeCategories?.forEach { category->
            addNewCategory(
                Categories(
                    categoryId = null,
                    blockId = blockId,
                    standardCategoryId = category.standard_category_id,
                    isIncome = true,
                    name = category.name
                )
            )
        }
        expCategories?.forEach { category->
            addNewCategory(
                Categories(
                    categoryId = null,
                    blockId = blockId,
                    standardCategoryId = category.standard_category_id,
                    isIncome = false,
                    name = category.name
                )
            )
        }
        return getListSections(
            sectionCategory = SECTION_CATEGORY,
            year = year,
            month = month
        )
    }

    @MapInfo(keyColumn = "date")
    @Query("WITH ft AS (SELECT * FROM transactions WHERE year=:year AND month IN (:months)), " +
            "fCat AS (SELECT * FROM categories WHERE category_id IN (:categoriesIds)) " +
            "SELECT ft.`current_date` AS date, ft.transaction_id AS id, ft.category_id AS categoryId, fCat.standard_category_id AS standard_category_id, " +
            "fCat.is_income AS isIncome, fCat.name AS categoryName, ft.comment AS comment, ft.amount as amount, ft.currency_name AS currency " +
            "FROM ft INNER JOIN fCat ON ft.category_id=fCat.category_id")
    fun getTransactionsFiltered(year: Int, months: List<Int>, categoriesIds: List<Int>): Flow<Map<Long, List<TransactionListItem>>>

    @MapInfo(keyColumn = "date")
    @Query("WITH ft AS (SELECT * FROM transactions WHERE (year=:year)) " +
            "SELECT ft.`current_date` AS date, ft.transaction_id AS id, ft.category_id AS categoryId, categories.standard_category_id AS standard_category_id, " +
            "categories.is_income AS isIncome, categories.name AS categoryName, ft.comment AS comment, ft.amount as amount, ft.currency_name AS currency " +
            "FROM ft INNER JOIN categories ON ft.category_id=categories.category_id")
    fun getTransactionsWithoutFilters(year: Int): Flow<Map<Long, List<TransactionListItem>>>

    @MapInfo(keyColumn = "date")
    @Query("WITH ft AS (SELECT * FROM transactions WHERE year=:year AND month IN (:months)) " +
            "SELECT ft.`current_date` AS date, ft.transaction_id AS id, ft.category_id AS categoryId, categories.standard_category_id AS standard_category_id, " +
            "categories.is_income AS isIncome, categories.name AS categoryName, ft.comment AS comment, ft.amount as amount, ft.currency_name AS currency " +
            "FROM ft INNER JOIN categories ON ft.category_id=categories.category_id")
    fun getTransactionsFilterMonths(year: Int, months: List<Int>): Flow<Map<Long, List<TransactionListItem>>>

    @MapInfo(keyColumn = "date")
    @Query("WITH ft AS (SELECT * FROM transactions WHERE year=:year ), " +
            "fCat AS (SELECT * FROM categories WHERE category_id IN (:categoriesIds)) " +
            "SELECT ft.`current_date` AS date, ft.transaction_id AS id, ft.category_id AS categoryId, fCat.standard_category_id AS standard_category_id, " +
            "fCat.is_income AS isIncome, fCat.name AS categoryName, ft.comment AS comment, ft.amount as amount, ft.currency_name AS currency " +
            "FROM ft INNER JOIN fCat ON ft.category_id=fCat.category_id")
    fun getTransactionsFilterCategories(year: Int, categoriesIds: List<Int>): Flow<Map<Long, List<TransactionListItem>>>

    @Query("SELECT block_id, category_id AS categoryId, standard_category_id, is_income AS isIncome FROM categories ")
    suspend fun getCategoriesList(): List<CategoriesFilter>

    @Query("SELECT DISTINCT year FROM transactions ")
    suspend fun  getYearsList(): List<Int>
}
