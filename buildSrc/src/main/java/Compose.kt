object Compose {
    // private const val activityComposeVersion = "1.3.0-rc01"
    private const val activityComposeVersion = "1.6.0-alpha03"
    const val activity = "androidx.activity:activity-compose:$activityComposeVersion"

    // const val composeVersion = "1.0.0"
    // const val composeVersion = "1.2.0-alpha07"
     const val composeVersion = "1.2.0-beta01"
    // const val composeVersion = "1.1.1"
    // const val composeVersion = "1.0.1"
    const val ui = "androidx.compose.ui:ui:$composeVersion"
    const val material = "androidx.compose.material:material:$composeVersion"
    const val tooling = "androidx.compose.ui:ui-tooling:$composeVersion"
    const val paging = "androidx.paging:paging-compose:$composeVersion"
    const val icon = "androidx.compose.material:material-icons-extended:$composeVersion"
    // private const val navigationVersion = "2.4.0-alpha04"
    private const val navigationVersion = "2.4.2"
    const val navigation = "androidx.navigation:navigation-compose:$navigationVersion"

    // private const val hiltNavigationComposeVersion = "1.0.0-alpha03"
    private const val hiltNavigationComposeVersion = "1.0.0"
    const val hiltNavigation = "androidx.hilt:hilt-navigation-compose:$hiltNavigationComposeVersion"
}

object ComposeTest {
    const val uiTestJunit4 = "androidx.compose.ui:ui-test-junit4:${Compose.composeVersion}"
    const val uiTestManifest = "androidx.compose.ui:ui-test-manifest:${Compose.composeVersion}"
}