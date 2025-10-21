import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.thenAnswer
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var mockLedgerRepository: ILedgerRepository
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockLedgerRepository = mock<ILedgerRepository>()
        viewModel = SearchViewModel(mockLedgerRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is empty search text and empty results`() = runTest {
        assertEquals("", viewModel.searchText.first())
        assertTrue(viewModel.searchResults.first().isEmpty())
    }

    @Test
    fun `onSearchTextChanged updates search text`() = runTest {
        viewModel.onSearchTextChanged("test")
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals("test", viewModel.searchText.first())
    }

    @Test
    fun `search with empty query returns all balances`() = runTest {
        val allBalances = listOf(
            Balance("Alice", 100.0),
            Balance("Bob", 200.0)
        )
        whenever(mockLedgerRepository.getAllBalances()).thenReturn(allBalances)

        viewModel.onSearchTextChanged("")
        viewModel.loadAllBalances()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(allBalances, viewModel.searchResults.first())
    }

    @Test
    fun `search filters balances by name`() = runTest {
        val allBalances = listOf(
            Balance("Alice", 100.0),
            Balance("Bob", 200.0),
            Balance("Charlie", 300.0)
        )
        whenever(mockLedgerRepository.getAllBalances()).thenReturn(allBalances)

        viewModel.loadAllBalances()
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.onSearchTextChanged("ali")
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(listOf(Balance("Alice", 100.0)), viewModel.searchResults.first())
    }

    @Test
    fun `search is case-insensitive`() = runTest {
        val allBalances = listOf(
            Balance("Alice", 100.0),
            Balance("bob", 200.0)
        )
        whenever(mockLedgerRepository.getAllBalances()).thenReturn(allBalances)

        viewModel.loadAllBalances()
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.onSearchTextChanged("BOB")
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(listOf(Balance("bob", 200.0)), viewModel.searchResults.first())
    }

    @Test
    fun `search with no matching results returns empty list`() = runTest {
        val allBalances = listOf(
            Balance("Alice", 100.0)
        )
        whenever(mockLedgerRepository.getAllBalances()).thenReturn(allBalances)

        viewModel.loadAllBalances()
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.onSearchTextChanged("xyz")
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.searchResults.first().isEmpty())
    }

    @Test
    fun `loading state is true during balance loading`() = runTest {
        // Simulate a delay in getAllBalances
        whenever(mockLedgerRepository.getAllBalances()).thenAnswer {
            delay(1000) // Simulate a suspending call
            emptyList()
        }

        val job = viewModel.loadAllBalances()
        // After launching, isLoading should be true
        assertTrue(viewModel.isLoading.value)

        // Advance time to allow the delay to complete
        testDispatcher.scheduler.advanceTimeBy(1000)
        job.join() // Wait for the coroutine to complete

        // After completion, isLoading should be false
        assertTrue(!viewModel.isLoading.value)
    }

    @Test
    fun `error state is set when repository throws exception`() = runTest {
        val errorMessage = "Network error"
        whenever(mockLedgerRepository.getAllBalances()).thenThrow(RuntimeException(errorMessage))

        viewModel.loadAllBalances()

        assertEquals(errorMessage, viewModel.error.value)
    }
}
