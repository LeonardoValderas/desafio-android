package com.picpay.desafio.android.presenter

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.LargeTest
import org.junit.runner.RunWith
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.internal.runner.junit4.statement.UiThreadStatement
import androidx.test.platform.app.InstrumentationRegistry
import com.picpay.desafio.android.MockModels.getUser
import com.picpay.desafio.android.R
import com.picpay.desafio.android.RecyclerViewMatchers
import com.picpay.desafio.android.core.data.network.response.DataResponse
import com.picpay.desafio.android.domain.model.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.*
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@LargeTest
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest : KoinTest {
    @Mock
    lateinit var viewModel: UserViewModel
    private var users = MutableLiveData<DataResponse<List<User>>>()
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        initViewModel()
        initModule()
    }

    //region TITLE
    @Test
    fun shouldDisplayTitle() {
        launchActivity<MainActivity>().apply {
            val expectedTitle = context.getString(R.string.title)

            moveToState(Lifecycle.State.RESUMED)

            Espresso.onView(ViewMatchers.withText(expectedTitle))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
    }
    //endregion

    //region RECYCLER
    @Test
    fun whenIsSuccessShouldDisplaySpecificItem() {
        launchActivity<MainActivity>()
        val user = getUser()
        UiThreadStatement.runOnUiThread {
            users.value = DataResponse.SUCCESS(
                listOf(user)
            )
        }

        RecyclerViewMatchers.checkRecyclerViewItem(
            R.id.recyclerView,
            0,
            ViewMatchers.withText(user.name)
        )

        //or
        // onView(withText("Eduardo Santos")).check(matches(isDisplayed()))
    }

    @Test
    fun whenIsSuccessShouldDisplayOneItem() {
        launchActivity<MainActivity>().apply {
            UiThreadStatement.runOnUiThread {
                users.value = DataResponse.SUCCESS(
                    listOf(getUser())
                )
            }

            moveToState(Lifecycle.State.RESUMED)

            onActivity {
                val recycler = it.findViewById<RecyclerView>(R.id.recyclerView)
                val adapter = recycler.adapter
                Assert.assertEquals(1, adapter?.itemCount)
            }
        }
    }

    @Test
    fun whenIsErrorShouldBeVisibilityGone() {
        launchActivity<MainActivity>().apply {
            UiThreadStatement.runOnUiThread {
                users.value = DataResponse.ERROR("Error")
            }

            moveToState(Lifecycle.State.RESUMED)

            onActivity {
                val recycler = it.findViewById<RecyclerView>(R.id.recyclerView)
                Assert.assertEquals(View.GONE, recycler.visibility)
            }
            //or
            // onView(withText("Eduardo Santos")).check(matches(isNotDisplayed()))
        }
    }
    //endregion

    @After
    fun finish() {
        //  server.shutdown()
    }

    //region PRIVATE
    private fun initViewModel() {
        Mockito.`when`(viewModel.users).thenReturn(users)
    }

    private fun initModule() {
        val mockedModule = module {
            single(override = true) { viewModel }
        }
        loadKoinModules(mockedModule)
    }
    //endregion
}