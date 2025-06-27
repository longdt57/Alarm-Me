package leegroup.module.test

import org.junit.Rule

abstract class BaseUnitTest {

    @get:Rule
    val coroutinesRule = CoroutineTestRule()

    val testDispatcherProvider = coroutinesRule.testDispatcherProvider
}