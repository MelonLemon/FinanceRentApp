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
    fun getSectionTransactionsBYMonth_checkResult() = runBlocking {
        val dictionary = TestDictionary()
        rentDao.addNewBlock(dictionary.sampleBlock)
        rentDao.addNewBlock(dictionary.sampleBlock2)
        rentDao.addNewBlock(dictionary.sampleBlock3)
        rentDao.addNewCategory(dictionary.sampleExpensesCategories)
        rentDao.addNewCategory(dictionary.sampleExpensesCategories2)
        rentDao.addNewCategory(dictionary.sampleExpensesCategories3)
        rentDao.addNewCategory(dictionary.sampleExpensesCategories4)
        rentDao.addNewTransaction(dictionary.sampleTransactions)
        rentDao.addNewTransaction(dictionary.sampleTransactions.copy(
            categoryId = 1,
            amount = -150
        ))
        rentDao.addNewTransaction(
            dictionary.sampleTransactions.copy(
                blockId = 2,
                categoryId = 4
            )
        )

        val outputMap = rentDao.getSectionTransactionsBYMonth(
            year = 2023,
            month= 10,
            blocksId = listOf(1, 3)
        )

        println("Start date: $outputMap")
        assertEquals(1, outputMap.size)

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
    val today = LocalDate.now().toEpochDay()
    val sampleBlock = Blocks(
        blockId = 1,
        blockCategory = FLAT_CATEGORY,
        name = "First Flat"
    )
    val sampleBlock2 = Blocks(
        blockId = 2,
        blockCategory = FLAT_CATEGORY,
        name = "Second Flat"
    )
    val sampleBlock3 = Blocks(
        blockId = 3,
        blockCategory = SECTION_CATEGORY,
        name = "Second Flat"
    )
    val sampleExpensesCategories = Categories(
        categoryId = 1,
        blockId = 1,
        standardCategoryId = 1,
        isIncome = false,
        name = "Expenses main"
    )
    val sampleExpensesCategories2 = Categories(
        categoryId = 2,
        blockId = 2,
        standardCategoryId = 1,
        isIncome = false,
        name = "Expenses main"
    )
    val sampleExpensesCategories3 = Categories(
        categoryId = 3,
        blockId = 1,
        standardCategoryId = 1,
        isIncome = true,
        name = "Income main"
    )
    val sampleExpensesCategories4 = Categories(
        categoryId = 4,
        blockId = 2,
        standardCategoryId = 1,
        isIncome = true,
        name = "Income main"
    )

    val sampleTransactions = Transactions(
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

}