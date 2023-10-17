package com.core.data.data_source

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.core.data.data_source.util.FLAT_CATEGORY
import com.core.data.data_source.util.SECTION_CATEGORY
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith
import java.time.LocalDate


@RunWith(AndroidJUnit4::class)
@SmallTest
class RentCountDaoTest{

    @get:Rule
    val dispatcherRule = TestDispatcherRule()

    private lateinit var rentDatabase: RentCountDatabase
    private lateinit var rentDao: RentCountDao

    @Before
    fun setUp() {
        rentDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RentCountDatabase::class.java
        ).allowMainThreadQueries().build()
        rentDao = rentDatabase.rentCountDao
    }

    @Test
    fun getAddRent_checkResult() = runBlocking {
        val dictionary = TestDictionary()
        rentDao.addNewBlock(dictionary.flat1)
        rentDao.addNewCategory(dictionary.incomeCatFlat1)
        val rentId =  rentDao.addNewRent(
            rents = dictionary.rentsFlat1
        ).toInt()
        rentDao.addRentTrack(
            rentsTrack = dictionary.rentsTrackFlat1.copy(
                rentId = rentId
            )
        )
        println("RentId: $rentId")
    }


    @Test
    fun getSectionTransactionsBYMonth_checkResult() = runBlocking {
        val dictionary = TestDictionary()


    }


    @After
    fun tearDown() {
        rentDatabase.close()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class TestDispatcherRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
): TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

class TestDictionary{
    private val today = LocalDate.now().toEpochDay()
    private val todayPlus = LocalDate.now().plusDays(6).toEpochDay()
    val flat1 = Blocks(
        blockId = 1,
        blockCategory = FLAT_CATEGORY,
        name = "First Flat"
    )
    val flat2 = Blocks(
        blockId = 2,
        blockCategory = FLAT_CATEGORY,
        name = "Second Flat"
    )
    val section1 = Blocks(
        blockId = 3,
        blockCategory = SECTION_CATEGORY,
        name = "Second Flat"
    )
    val expensesFlat1 = Categories(
        categoryId = 1,
        blockId = 1,
        standardCategoryId = 1,
        isIncome = false,
        name = "Expenses main"
    )
    val expensesFlat2 = Categories(
        categoryId = 2,
        blockId = 2,
        standardCategoryId = 1,
        isIncome = false,
        name = "Expenses main"
    )
    val incomeCatFlat1 = Categories(
        categoryId = 3,
        blockId = 1,
        standardCategoryId = 1,
        isIncome = true,
        name = "Income main"
    )
    val incomeCatFlat2 = Categories(
        categoryId = 4,
        blockId = 2,
        standardCategoryId = 1,
        isIncome = true,
        name = "Income main"
    )

    val transactionIncomeFlat1 = Transactions(
        transactionId = null,
        blockId = 1,
        categoryId = 3,
        amount = 5000,
        currency_name = "US",
        year=2023,
        month = 10,
        currentDate = today,
        comment = ""
    )

    val rentsFlat1 = Rents(
        rentId = null,
        blockId = flat1.blockId!!,
        name="Rita",
        phone="9067856467",
        comment="With Dog",
        startDate = today,
        endDate = todayPlus,
        forNight = 1000,
        forAllNights = 6000,
        nights=6,
        isPaid=false
    )

    val rentsTrackFlat1 = RentsTrack(
        trackId = null,
        rentId = -1,
        year=2023,
        month=10,
        nights=6,
        amount=6000,
        isPaid = false,
        transaction_id = null
    )

}