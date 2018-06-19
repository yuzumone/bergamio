package net.yuzumone.bergamio.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    companion object {
        val CACHE_FILE_NAME = "okhttp.cache"
        val MAX_CACHE_SIZE = (4 * 1024 * 1024).toLong()
    }

    @Provides
    fun provideApplicationContext(): Context {
        return application
    }

    @Singleton
    @Provides
    fun provideHttpClient(context: Context): OkHttpClient {
        val cacheDir = File(context.cacheDir, CACHE_FILE_NAME)
        val cache = Cache(cacheDir, MAX_CACHE_SIZE)
        val client = OkHttpClient.Builder()
                .cache(cache)
        return client.build()
    }

    @Provides
    fun provideCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }
}