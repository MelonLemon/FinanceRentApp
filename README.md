# FinanceRentApp
Helps track rent out income/expenses and others income/expenses 
**FinanceRentApp** is a multimodule system that uses Jetpack Compose, Dagger Hilt, Room, kotlin Coroutines.

# Screenrecorder, Redmi Xiaomi

https://github.com/MelonLemon/FinanceRentApp/assets/26432711/a0ca8625-cbc4-43fc-a5e1-256f91907b2f

# Modularization
App has 3 feature: Home, Transactions and Analytics.  As we could see in scheme App is following Clean Architecture rules. All of them have 2 layers domain and presentation.
I created di's (dagger-hilt)  in :core:data and :feature_home:domain:, :feature_transactions:domain to connect data.

![rent_diagram](https://github.com/MelonLemon/FinanceRentApp/assets/26432711/52fddcaf-95ac-421f-8305-661ce919f780)



# Database
I used room(sqlite database). Tables for database I placed in :core:data module. That's way implementation of database do not depend on domain or presentation layer.
Meanwhile, ui_model(business logic) - data classes - I placed in domain layer. 
That's way we can change data layer independently - change from one implementation to another 
or changing ui_models mostly will lead to change in queries rather models scheme of database. 
![rent db diagram](https://github.com/MelonLemon/FinanceRentApp/assets/26432711/5a1072ef-56a4-4bd1-a948-58e3beb3447b)


# Navigation
For each feature we create seperate Navigation Graph. In Presentation layer we create extention of NavGraph where we implement all navigation inside one feature(between screens of 1 feature).
Navigation between different features is implemented at a higher level (app layer).
In Presentation layer - we create extention for all screens. 
**Example**:
```
fun NavGraphBuilder.homeScreen(
    toFlatScreen: (Int) -> Unit
) {

    composable(route = homeRoute) {
        val viewModel = hiltViewModel<HomeViewModel>()
        val homeState by viewModel.homeState.collectAsStateWithLifecycle()
        val homeEvents = viewModel::homeScreenEvents
        val homeUiEvents by viewModel::onHomeUiEvents
        HomeScreen(
            homeState=homeState,
            homeEvents=homeEvents,
            homeUiEvents=homeUiEvents,
            toFlatScreen=toFlatScreen
        )
    }
}
```
We as well can create custome navigation to screens 
```
fun NavController.navigateToFlat(flatId: Int) {
    this.navigate("${flatRoute}/$flatId")
}
```
At the end we collect every scren in our Graph:
```
fun NavGraphBuilder.homeGraph(
    navController: NavController
) {
    navigation(
        startDestination = homeRoute,
        route = HomePattern
    ) {
        homeScreen(
            toFlatScreen = {id ->
                navController.navigateToFlat(id)
            }
        )
        flatScreen(
           backToHomeScreen = {
               navController.popBackStack()
           }
        )

    }
}
```
In App layer we create BottomNavigation and NavHost
```
NavHost(
    modifier = Modifier.padding(innerPadding),
    navController = navController,
    startDestination = HomePattern,
){
    transactionGraph(navController = navController)
    homeGraph(navController = navController)
    analyticsGraph(navController = navController)
}
```
# Transaction feature with Coroutines (ViewModel)
We have 3 main states: filterState, filteredTransactionsByMonth and transactionsByMonth after search by text. 
filterState is a StateFlow that we change. 
filteredTransactionsByMonth depends on filterState and is flow. 
```
private val _filteredTransactionsByMonth = filterState.flatMapLatest{ filterState->
        useCases.getFilteredTransactions(
            year = filterState.periodFilterState.selectedYear,
            months = if(filterState.periodFilterState.isAllMonthsSelected) null else filterState.periodFilterState.months,
            currency = currency.value,
            categoriesIds = useCases.getFilteredCategoriesId(
                categoriesFilterList = filterState.categoriesFilterList,
                blockIds = if(filterState.sectionsFilterState.isAllSelected) null else filterState.sectionsFilterState.listOfSelectedBlIds,
                selectedIncomeCatId = if(filterState.categoryFilterState.isAllIncomeSelected) null else filterState.categoryFilterState.selectedIncomeCatId,
                selectedExpensesCatId = if(filterState.categoryFilterState.isAllExpensesSelected) null else filterState.categoryFilterState.selectedExpensesCatId
            )
        )
    }.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5000),
    emptyList()
    )
```
transactionsByMonth depends on search text - depends on change of search text it will take filteredTransactionsByMonth and result in matching transactions. 
```
private val transactionsByMonth  = searchText
        .debounce(500L)
        .onEach { _isDownloading.update { true } }
        .combine(_filteredTransactionsByMonth)
        { searchText, transactionsByMonth  ->
            useCases.getSearchedTransactions(
                searchText = searchText,
                transactionMonth = transactionsByMonth)
        }.onEach { _isDownloading.update { false } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _filteredTransactionsByMonth.value
        )
```
Than we combine flows for our main ui state:
```
  val transactionState  = combine(searchText,  filterState, transactionsByMonth, _isDownloading){searchText,  filterState, transactionsByMonth, isDownloading ->
        TransactionState(
            searchText = searchText,
            isDownloading = isDownloading,
            filterAmount = if(transactionsByMonth.isNotEmpty()){

                if(filterState.periodFilterState.isAllMonthsSelected) {
                    transactionsByMonth.sumOf { it.amount }
                } else {
                    transactionsByMonth.filter { it.month in filterState.periodFilterState.months }
                        .sumOf { it.amount }}
            } else {
                0
            },
            transactionsByMonth = transactionsByMonth,
            filterState = filterState
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        TransactionState(
            filterState = FilterState(
                periodFilterState = PeriodFilterState(
                    years = listOf(currentMonth.year),
                    selectedYear = currentMonth.year)
            )
        )
    )
```
