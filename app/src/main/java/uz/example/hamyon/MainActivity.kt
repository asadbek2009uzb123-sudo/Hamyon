package uz.example.hamyon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HamyonApp()
        }
    }
}

@Composable
fun HamyonApp() {
    var balance by remember { mutableStateOf(1500000L) }
    var amount by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var history by remember { mutableStateOf("Ilova ishga tushirildi") }

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("HAMYON", fontSize = 30.sp)
            Text("SINOV HISOBI • HAQIQIY TO‘LOV EMAS")

            Spacer(modifier = Modifier.height(25.dp))

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Hisobdagi mablag‘")
                    Text(
                        "%,d so‘m".format(balance),
                        fontSize = 28.sp
                    )
                    Text("Bu faqat sinov uchun berilgan mablag‘.")
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            Text("Sinov o‘tkazmasi", fontSize = 22.sp)

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Miqdorni kiriting") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    val value = amount.toLongOrNull()
                    if (value != null && value > 0 && value <= balance) {
                        balance -= value
                        history = "Sinov o‘tkazmasi: %,d so‘m".format(value)
                        message = "Sinov muvaffaqiyatli bajarildi!"
                        amount = ""
                    } else {
                        message = "Miqdorni tekshiring!"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sinov o‘tkazmasini bajarish")
            }

            Spacer(modifier = Modifier.height(15.dp))

            Text(message)
            Text("Oxirgi operatsiya:", fontSize = 18.sp)
            Text(history)

            Spacer(modifier = Modifier.height(25.dp))

            Text("To‘lov xizmatlari", fontSize = 22.sp)
            Text("Mobil aloqa  •  Internet")
            Text("Kommunal xizmatlar  •  Jarimalar")

            Spacer(modifier = Modifier.height(20.dp))

            Text("Barcha amallar faqat demo rejimida ishlaydi.")
        }
    }
}
