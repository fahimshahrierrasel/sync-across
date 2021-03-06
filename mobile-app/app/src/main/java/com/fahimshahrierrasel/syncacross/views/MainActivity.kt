package com.fahimshahrierrasel.syncacross.views

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.files.fileChooser
import com.fahimshahrierrasel.syncacross.R
import com.fahimshahrierrasel.syncacross.config.FirebaseConfig
import com.fahimshahrierrasel.syncacross.databinding.ActivityMainBinding
import com.fahimshahrierrasel.syncacross.models.Constants
import com.fahimshahrierrasel.syncacross.models.ItemType
import com.fahimshahrierrasel.syncacross.models.SyncItem
import com.fahimshahrierrasel.syncacross.models.imageExtensions
import com.fahimshahrierrasel.syncacross.utils.readBoolFromSharedPreference
import com.fahimshahrierrasel.syncacross.utils.toEditable
import com.fahimshahrierrasel.syncacross.utils.writeBoolToSharedPreference
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.Query
import kotlinx.coroutines.*
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var adapter: SyncItemRVAdapter
    val networkScope = CoroutineScope(Dispatchers.IO + Job())
    private lateinit var binding: ActivityMainBinding
    private val requiredPermissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    companion object {
        private val TAG = MainActivity::class.simpleName!!
        const val PERMISSION_CODE = 111
        lateinit var Instance: MainActivity
    }

    init {
        Instance = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (this.readBoolFromSharedPreference(Constants.THEME_KEY)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // No need to panic about permission
        // I know how to give permission explicitly
        checkAndAskForPermission()

        if(intent?.action == Intent.ACTION_SEND) {
            intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
                Log.i("MyIntent", it)
                openNewMessageDialog(it)
            }
        }

        binding.newItemFab.setOnClickListener {
            mainItemOnClickListener(binding)
        }

        binding.textFab.setOnClickListener {
            textFabOnClickListener(binding)
        }

        binding.fileFab.setOnClickListener {
            fileFabOnclickListener(binding)
        }

        binding.swipeRefresh.setOnRefreshListener {
            adapter.refresh()

            val timer = object : CountDownTimer(3000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    return
                }

                override fun onFinish() {
                    binding.swipeRefresh.isRefreshing = false
                }
            }
            timer.start()
        }

        binding.rvMainItems.setOnScrollChangeListener { _, _, _, _, oldScrollY ->
            if (oldScrollY < 0) {
                binding.newItemFab.isExpanded = false
                binding.newItemFab.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_add
                    )
                )
            }
        }

        binding.rvMainItems.layoutManager = LinearLayoutManager(this)

        val query =
            FirebaseConfig.messageCollectionReference()
                .orderBy("createdAt", Query.Direction.DESCENDING)

        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPrefetchDistance(10)
            .setPageSize(10)
            .build()

        val options = FirestorePagingOptions.Builder<SyncItem>()
            .setLifecycleOwner(this)
            .setQuery(query, pagedListConfig, SyncItem::class.java)
            .build()

        adapter = SyncItemRVAdapter(options)
        binding.rvMainItems.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val menuItem: MenuItem = menu!!.findItem(R.id.theme_changer)
        menuItem.setActionView(R.layout.appbar_switch)

        val swtThemeChange = menuItem.actionView.findViewById<SwitchMaterial>(R.id.swt_theme_change)
        swtThemeChange.isChecked = this.readBoolFromSharedPreference(Constants.THEME_KEY)
        swtThemeChange.setOnCheckedChangeListener { _, isChecked ->
            this.writeBoolToSharedPreference(Constants.THEME_KEY, isChecked)
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
            }
            delegate.applyDayNight()
            recreate()

        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                FirebaseConfig.auth.signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun checkAndAskForPermission() {
        val permissionNeeded = arrayListOf<String>()
        requiredPermissions.forEach {
            if (ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_DENIED) {
                permissionNeeded.add(it)
            }
        }

        if (permissionNeeded.size > 0) {
            ActivityCompat.requestPermissions(
                this,
                permissionNeeded.toTypedArray(),
                PERMISSION_CODE
            )
        }
    }

    private fun textFabOnClickListener(binding: ActivityMainBinding) {
        mainItemOnClickListener(binding)
        openNewMessageDialog()
    }

    private fun openNewMessageDialog(message: String = "") {
        val newMessageView = layoutInflater.inflate(R.layout.new_message_input, binding.root, false)
        val messageTextField = newMessageView.findViewById<TextInputLayout>(R.id.tf_message)
        messageTextField.editText?.text = message.toEditable()

        MaterialDialog(this).show {
            title(R.string.new_message)
            customView(view = newMessageView)
            positiveButton(R.string.submit, click = {
                val customView = this.getCustomView()
                val tf = customView.findViewById<TextInputLayout>(R.id.tf_message)

                val item = SyncItem(
                    ItemType.MESSAGE,
                    tf.editText?.text.toString(),
                    Build.MODEL,
                    Calendar.getInstance(TimeZone.getTimeZone("UTC")).time
                )
                networkScope.launch {
                    FirebaseConfig.createMessage(item)
                    adapter.refresh()
                    runOnUiThread {
                        Toast.makeText(
                            this@MainActivity,
                            "Created",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                Log.i("SYNC_ITEM", item.toFirebaseObject().toString())
            })
            negativeButton(R.string.cancel)
        }
    }

    private fun fileFabOnclickListener(binding: ActivityMainBinding) {
        mainItemOnClickListener(binding)

        val file = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            @Suppress("DEPRECATION")
            Environment.getExternalStorageDirectory()
        } else {
            this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        }

        MaterialDialog(this).show {
            fileChooser(
                initialDirectory = file,
                context = this@MainActivity,
                waitForPositiveButton = true
            ) { _, file ->
                // File selected
                val fileUri = Uri.fromFile(file)
                val currentDateTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                val fileRef =
                    FirebaseConfig.syncFilesRef.child("${currentDateTime.timeInMillis}.${file.extension}")
                try {
                    val uploadTask = fileRef.putFile(fileUri)
                    uploadTask.continueWithTask { task ->
                        if (!task.isSuccessful) {
                            task.exception?.let {
                                throw it
                            }
                        }
                        fileRef.downloadUrl
                    }.addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            val type = if (imageExtensions.contains(file.extension)) {
                                ItemType.IMAGE
                            } else {
                                ItemType.FILE
                            }

                            val downloadUri = task.result
                            val item = SyncItem(
                                type,
                                downloadUri.toString(),
                                Build.MODEL,
                                currentDateTime.time
                            )
                            networkScope.launch {
                                FirebaseConfig.createMessage(item)
                                adapter.refresh()
                                runOnUiThread {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Uploaded ${file.extension}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } else {
                            Log.e(TAG, "File Upload Failure")
                        }
                    }
                } catch (ex: Exception) {
                    Log.e(TAG, ex.message, ex)
                }
            }
        }
    }

    private fun mainItemOnClickListener(binding: ActivityMainBinding) {
        binding.newItemFab.isExpanded = !binding.newItemFab.isExpanded
        if (binding.newItemFab.isExpanded) {
            binding.newItemFab.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_close
                )
            )
        } else {
            binding.newItemFab.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_add
                )
            )
        }
    }

    fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}