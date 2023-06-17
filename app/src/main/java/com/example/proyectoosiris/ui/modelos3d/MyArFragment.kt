package com.example.proyectoosiris.ui.modelos3d

import android.R
import android.media.MediaPlayer.OnCompletionListener
import android.os.Bundle

/*
// Create a custom ArFragment class that implements OnCompletionListener
class MyArFragment : ArFragment(), OnCompletionListener {
    // Override the onAttach() method to register the listener
    fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCompletionListener) {
            (context as OnCompletionListener).onCompletion(this)
        }
    }

    // Override the onDetach() method to unregister the listener
    fun onDetach() {
        super.onDetach()
        if (getActivity() is OnCompletionListener) {
            (getActivity() as OnCompletionListener).onCompletion(null)
        }
    }

    // Override the getSessionConfiguration() method to configure the ARCore session
    protected fun getSessionConfiguration(session: Session?): Config {
        val config: Config = super.getSessionConfiguration(session)
        config.setUpdateMode(Config.UpdateMode.LATEST_CAMERA_IMAGE)
        return config
    }

    // Define an interface for the listener
    interface OnCompletionListener {
        fun onCompletion(fragment: MyArFragment?)
    }
}

open class ArFragment {

}

// In your activity, add the fragment using a FragmentTransaction
class MyActivity : OnCompletionListener {
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Add the fragment to the layout
        getSupportFragmentManager().beginTransaction()
            .add(R.id.fragment_holder, MyArFragment(), "my_arfragment")
            .commit()
    }

    // Implement the onCompletion() method to handle the fragment events
    override fun onCompletion(fragment: MyArFragment?) {
        if (fragment != null) {
            // The fragment is ready, you can use it to render AR scenes
        } else {
            // The fragment is detached, you should clean up any resources
        }
    }
}*/