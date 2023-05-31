package ayds.newyork.songinfo.moredetails.presentation.presenter

import ayds.newyork.songinfo.moredetails.domain.entities.Card
import ayds.newyork.songinfo.moredetails.domain.entities.Card.ArtistCard
import ayds.newyork.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.newyork.songinfo.moredetails.presentation.MoreDetailsUIState
import ayds.observer.Subject
import ayds.observer.Observable

interface MoreDetailsPresenter {

    val uiStateObservable: Observable<MoreDetailsUIState>
    val onUIStateSubject: Subject<MoreDetailsUIState>
    var uiState: MoreDetailsUIState
    fun openArtistInfoWindow(artistName: String)
}

internal class MoreDetailsPresenterImpl(
    private val repository: ArtistRepository,
    private val formatter: RepositoryToViewFormatter
) : MoreDetailsPresenter {
    override val onUIStateSubject = Subject<MoreDetailsUIState>()
    override val uiStateObservable = onUIStateSubject
    override var uiState: MoreDetailsUIState = MoreDetailsUIState(mutableListOf())

    override fun openArtistInfoWindow(artistName: String) {
        Thread {
            loadArtistInfo(artistName)
        }.start()
    }

    private fun loadArtistInfo(artistName: String) {
        val artistData = repository.getArtistData(artistName)
        for (card in artistData) {
            updateUIState(card, artistName)
        }
        uiStateObservable.notify(uiState)
    }

    private fun updateUIState(artist: Card, artistName: String) {
        when (artist) {
            is ArtistCard -> {
                val formattedArtist =
                    ArtistCard(
                        description = formatter.format(artist, artistName),
                        infoUrl = artist.infoUrl,
                        source = artist.source,
                        sourceLogoUrl = artist.sourceLogoUrl,
                        isInDatabase = artist.isInDatabase

                    )
                uiState = uiState.copy(artistCards = uiState.artistCards.toMutableList().apply { add(formattedArtist) })
            }
            else -> {}
        }
    }
}