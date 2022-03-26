package onlyTest

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jiayx.wlan.databinding.LibWifiActivityOnlytestTestBinding


class TestActivity : AppCompatActivity() {
    private val binding: LibWifiActivityOnlytestTestBinding by lazy {
        LibWifiActivityOnlytestTestBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.wifiTest.setOnClickListener {
                startActivity(WifiActivityKt::class.java)
        }
        binding.softInputTest.setOnClickListener {
            startActivity(SoftInputActivity::class.java)
        }
    }

    private fun <T> startActivity(a: Class<T>) {
        val intent = Intent(this, a)
        startActivity(intent)
    }

}