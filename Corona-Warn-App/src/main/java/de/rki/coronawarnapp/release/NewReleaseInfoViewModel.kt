package de.rki.coronawarnapp.release

import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import de.rki.coronawarnapp.BuildConfig
import de.rki.coronawarnapp.R
import de.rki.coronawarnapp.environment.BuildConfigWrap
import de.rki.coronawarnapp.main.CWASettings
import de.rki.coronawarnapp.util.coroutine.DispatcherProvider
import de.rki.coronawarnapp.util.ui.SingleLiveEvent
import de.rki.coronawarnapp.util.ui.toResolvingString
import de.rki.coronawarnapp.util.viewmodel.CWAViewModel
import de.rki.coronawarnapp.util.viewmodel.SimpleCWAViewModelFactory
import timber.log.Timber

class NewReleaseInfoViewModel @AssistedInject constructor(
    dispatcherProvider: DispatcherProvider,
    private val appSettings: CWASettings
) : CWAViewModel(dispatcherProvider = dispatcherProvider) {

    val routeToScreen: SingleLiveEvent<NewReleaseInfoNavigationEvents> = SingleLiveEvent()

    val title = R.string.release_info_version_title.toResolvingString(BuildConfig.VERSION_NAME)

    fun onNextButtonClick(comesFromInfoScreen: Boolean) {
        appSettings.lastChangelogVersion.update { BuildConfigWrap.VERSION_CODE }
        if (appSettings.lastNotificationsOnboardingVersionCode.value == 0L && !comesFromInfoScreen) {
            routeToScreen.postValue(
                NewReleaseInfoNavigationEvents.NavigateToOnboardingDeltaNotificationsFragment
            )
        } else {
            routeToScreen.postValue(NewReleaseInfoNavigationEvents.CloseScreen)
        }
    }

    fun getItems(
        titles: Array<String>,
        bodies: Array<String>,
        linkifiedLabels: Array<String>,
        linkTargets: Array<String>
    ): List<NewReleaseInfoItem> {
        if (titles.size != bodies.size || titles.size != linkifiedLabels.size || titles.size != linkTargets.size) {
            Timber.e(
                "R.array.new_release_title AND R.array.new_release_body AND " +
                    "R.array.new_release_linkified_AND R.array.new_release_target_urls arrays must have the same size!"
            )
            return emptyList()
        }
        val items = mutableListOf<NewReleaseInfoItem>()
        titles.indices.forEach { i ->
            if (linkifiedLabels[i].isNullOrBlank() || linkTargets[i].isNullOrBlank()) {
                items.add(NewReleaseInfoItemText(titles[i], bodies[i]))
            } else {
                items.add(NewReleaseInfoItemLinked(titles[i], bodies[i], linkifiedLabels[i], linkTargets[i]))
            }
        }
        return items
    }

    @AssistedFactory
    interface Factory : SimpleCWAViewModelFactory<NewReleaseInfoViewModel>
}
