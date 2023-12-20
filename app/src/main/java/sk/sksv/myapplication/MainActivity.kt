package sk.sksv.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import sk.sksv.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding!!

    private var list: MutableLiveData<MutableSet<String>> = MutableLiveData()


    private val registerActivityForResult = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            list.postValue(getContactList())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        list.postValue(
            getContactList()
        )
        list.observe(this) {
            it?.let {
                binding.rvContacts.adapter = MainAdapter(it.toList())
            }
        }
    }


    @SuppressLint("Range", "Recycle")
    private fun getContactList(): MutableSet<String> {
        val set = mutableSetOf<String>()
        sdkIntAboveOreo {

            //by using call fun we check for permission requested
            isPermissionGranted(this, android.Manifest.permission.READ_CONTACTS) {

                if (it) {

                    //Return a ContentResolver instance for your application's package.
                    //we use contentResolver to send request to get data from other application
                    val contentResolver = applicationContext.contentResolver

                    //by using Uri property of contentResolver query() function we request for contact data
                    val cursor =
                        contentResolver.query(
                            ContactsContract.Contacts.CONTENT_URI,
                            null,
                            null,
                            null
                        )

                    // contentResolver query() function give us set od data where cursor point at the start ot data
                    if (cursor?.moveToFirst() == true) {
                        do {
                            val name = cursor.getString(
                                cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                            )
                            set.add(name)
                        } while (cursor.moveToNext())
                    }

                } else {
                    registerActivityForResult.launch(android.Manifest.permission.READ_CONTACTS)
                }
            }


        }
        return set
    }

    inline fun sdkIntAboveOreo(call: () -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            call.invoke()
        }
    }

    inline fun isPermissionGranted(context: Context, permission: String, call: (Boolean) -> Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            call.invoke(true)
        } else {
            call.invoke(false)
        }
    }
}