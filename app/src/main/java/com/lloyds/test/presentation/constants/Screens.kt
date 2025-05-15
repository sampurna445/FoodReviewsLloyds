package com.lloyds.test.presentation.constants

sealed class Screens(
    val route: String,
) {
    open fun createRoute(vararg args: Any) = route

    data object Reviews : Screens("Reviews")

    data object Detail : Screens("ReviewDetail/{reviewId}") {
        override fun createRoute(vararg args: Any): String =
            if (args.size == 1) {
                "ReviewDetail/${args[0]}"
            } else {
                throw Exception("Invalid number of arguments")
            }
    }
}
