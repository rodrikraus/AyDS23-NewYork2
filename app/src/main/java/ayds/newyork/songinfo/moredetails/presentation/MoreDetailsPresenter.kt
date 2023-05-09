package ayds.newyork.songinfo.moredetails.presentation

import ayds.newyork.songinfo.home.view.HomeUiEvent
import ayds.newyork.songinfo.moredetails.MoreDetailsInjector
import ayds.newyork.songinfo.moredetails.data.repository.*
import ayds.newyork.songinfo.moredetails.data.repository.local.sqldb.ArtistLocalStorageImpl
import ayds.newyork.songinfo.moredetails.data.repository.local.sqldb.CursorToArtistDataMapper
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistData
import ayds.newyork.songinfo.moredetails.domain.repository.ArtistRepository
import java.util.*


interface MoreDetailsPresenter {
    fun obtainArtistRepository()
    fun loadArtistInfo()
    fun setArtistName(artistName: String?)
}

internal class MoreDetailsPresenterImpl(private val moreDetailsView: MoreDetailsView, private val moreDetailsPresentation: MoreDetailsPresentation) : MoreDetailsPresenter {

    private lateinit var repository: ArtistRepository
    private var artistName: String? = null


    override fun obtainArtistRepository() {
        repository = moreDetailsPresentation.obtainArtistRepository()
    }

    override fun loadArtistInfo() {
        Thread {
            var artistData: ArtistData? = null
            if(artistName != null)
                artistData = repository.getArtistData(artistName!!)
            if(artistData != null)
                moreDetailsView.setView(artistData)
        }.start()
    }

    override fun setArtistName(artistName: String?) {
        this.artistName = artistName
    }


}