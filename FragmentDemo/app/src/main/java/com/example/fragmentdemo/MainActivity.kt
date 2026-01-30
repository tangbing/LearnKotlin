package com.example.fragmentdemo
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

        // 在 Activity 中得到相应的 Fragment 实例
        val leftFrg = supportFragmentManager.findFragmentById(R.id.leftFrag) as LeftFragment
//        leftFrg.button.setOnClickListener {
//            replaceFragment(AnotherRightFragment())
//        }
        //ccreplaceFragment(RightFragment())
    }

//    private fun replaceFragment(fragment: Fragment) {
//        val fragmentManager = supportFragmentManager
//        val transaction = fragmentManager.beginTransaction()
//        transaction.replace(R.id.rightLayout,fragment)
//        transaction.addToBackStack(null)
//        transaction.commit()
//    }

}