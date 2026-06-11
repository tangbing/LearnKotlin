package com.example.roomtest

import android.os.Bundle
import android.util.Log
import android.util.TimeUtils
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.work.BackoffPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.roomtest.ui.theme.RoomTestTheme
import com.example.roomtest.workManager.SimpleWorker
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_layout)

        val addBtn = findViewById<Button>(R.id.addBtn)
        val deleteBtn = findViewById<Button>(R.id.deleteBtn)
        val updateBtn = findViewById<Button>(R.id.updateBtn)
        val queryBtn = findViewById<Button>(R.id.queryBtn)
        val doWorkBtn = findViewById<Button>(R.id.doWorkBtn)
        val cancelDoWorkBtn = findViewById<Button>(R.id.cancelDoWorkBtn)


        val userDao = AppDatabase.getDatabase(this).userDao()
        val user1 = User("Tom", "Brady", 40)
        val user2 = User("Tom", "Hanks", 63)




        doWorkBtn.setOnClickListener {
//            val request = OneTimeWorkRequest.Builder(SimpleWorker::class.java).build()
            // 后台任务在 xx 分钟后运行
            val request = OneTimeWorkRequest.Builder(SimpleWorker::class.java)
                .setInitialDelay(1, TimeUnit.MINUTES)
                .addTag("simple")
                .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
                .build()

            WorkManager.getInstance(this).enqueue(request)

            WorkManager.getInstance(this).getWorkInfoByIdLiveData(request.id)
                .observe(this) { workInfo ->
                    if (workInfo?.state == WorkInfo.State.SUCCEEDED) {
                        "cancel simple Tag work SUCCEEDED".showToast()
//                        Toast.makeText(this, "cancel simple Tag work SUCCEEDED",  Toast.LENGTH_SHORT).show();

                    } else if (workInfo?.state == WorkInfo.State.FAILED) {
                        "cancel simple Tag work FAILED".showToast()
//                        Toast.makeText(this, "cancel simple Tag work FAILED",  Toast.LENGTH_SHORT).show();
                    }

            }

        }

        cancelDoWorkBtn.setOnClickListener {
            "cancel simple Tag work!".showToast()
           // Toast.makeText(this, "cancel simple Tag work!",  Toast.LENGTH_SHORT).show();
//            WorkManager.getInstance(this).cancelAllWorkByTag("simple")

            // 取消全部任务
            WorkManager.getInstance(this).cancelAllWork()
        }

        addBtn.setOnClickListener {
            thread {
                user1.id = userDao.insertUser(user1)
                user2.id = userDao.insertUser(user2)
            }
        }

        updateBtn.setOnClickListener {
            thread {
                user1.age = 42
                userDao.updateUser(user1)
            }
        }

        deleteBtn.setOnClickListener {
            thread {
                userDao.deleteUserByLastName("Hanks")
            }
        }

        queryBtn.setOnClickListener {
            thread {
              for (user in userDao.loadAllUsers()) {
                  Log.d("MainActivity", user.toString())
              }
            }
        }

    }
}
