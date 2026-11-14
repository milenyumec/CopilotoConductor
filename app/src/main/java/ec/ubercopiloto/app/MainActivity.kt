package ec.ubercopiloto.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.Locale
import kotlin.math.roundToInt

data class TripInput(
    val fare: Double,
    val pickupKm: Double,
    val passengerKm: Double,
    val minutes: Double,
    val fuelPricePerGallon: Double,
    val kmPerLiter: Double,
    val maintenancePerKm: Double
)

data class TripResult(
    val totalKm: Double,
    val fuelCost: Double,
    val maintenanceReserve: Double,
    val netAfterReserves: Double,
    val perKm: Double,
    val hourlyGross: Double
)

fun analyzeTrip(t: TripInput): TripResult {
    val totalKm = t.pickupKm + t.passengerKm
    val liters = if (t.kmPerLiter > 0) totalKm / t.kmPerLiter else 0.0
    val gallons = liters / 3.785411784
    val fuel = gallons * t.fuelPricePerGallon
    val reserve = totalKm * t.maintenancePerKm
    val durationHours = t.minutes / 60.0
    return TripResult(
        totalKm = totalKm,
        fuelCost = fuel,
        maintenanceReserve = reserve,
        netAfterReserves = t.fare - fuel - reserve,
        perKm = if (totalKm > 0) t.fare / totalKm else 0.0,
        hourlyGross = if (durationHours > 0) t.fare / durationHours else 0.0
    )
}

private fun money(v: Double) = "$" + String.format(Locale.US, "%.2f", v)
private fun number(v: Double) = String.format(Locale.US, "%.1f", v)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { CopilotoScreen() }
    }
}

@Composable
fun CopilotoScreen() {
    var fare by remember { mutableStateOf("4.38") }
    var pickup by remember { mutableStateOf("3") }
    var passenger by remember { mutableStateOf("15") }
    var minutes by remember { mutableStateOf("39") }
    var fuelPrice by remember { mutableStateOf("3") }
    var consumption by remember { mutableStateOf("9") }
    var maintenance by remember { mutableStateOf("0.10") }

    val input = TripInput(
        fare.toDoubleOrNull() ?: 0.0,
        pickup.toDoubleOrNull() ?: 0.0,
        passenger.toDoubleOrNull() ?: 0.0,
        minutes.toDoubleOrNull() ?: 0.0,
        fuelPrice.toDoubleOrNull() ?: 3.0,
        consumption.toDoubleOrNull() ?: 9.0,
        maintenance.toDoubleOrNull() ?: 0.10
    )
    val result = analyzeTrip(input)
    val viable = result.netAfterReserves > 0 && result.perKm >= 0.40

    MaterialTheme(colorScheme = darkColorScheme()) {
        Scaffold { padding ->
            Column(
                Modifier.padding(padding).fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("UBER COPILOTO", style = MaterialTheme.typography.headlineMedium)
                Text("Analiza antes de aceptar", color = MaterialTheme.colorScheme.secondary)
                OutlinedTextField(fare, { fare = it }, label = { Text("Pago que recibirías (US$)") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(pickup, { pickup = it }, label = { Text("Km hasta recoger") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(passenger, { passenger = it }, label = { Text("Km con pasajero") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(minutes, { minutes = it }, label = { Text("Duración total (minutos)") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(fuelPrice, { fuelPrice = it }, label = { Text("Gasolina por galón (US$)") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(consumption, { consumption = it }, label = { Text("Consumo (km/litro)") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(maintenance, { maintenance = it }, label = { Text("Reserva mantenimiento por km (US$)") }, modifier = Modifier.fillMaxWidth())

                HorizontalDivider()
                Text("RESULTADO ESTIMADO", style = MaterialTheme.typography.titleMedium)
                Text("Distancia total: ${number(result.totalKm)} km")
                Text("Combustible estimado: ${money(result.fuelCost)}")
                Text("Reserva mantenimiento: ${money(result.maintenanceReserve)}")
                Text("Ingreso bruto por km: ${money(result.perKm)}")
                Text("Ingreso bruto por hora: ${money(result.hourlyGross)}")
                Text("Resultado después de reservas: ${money(result.netAfterReserves)}",
                    style = MaterialTheme.typography.titleLarge)
                Surface(
                    color = if (viable) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer,
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        if (viable) "REVISAR: POSIBLEMENTE CONVIENE" else "ALERTA: RENTABILIDAD BAJA",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Text(
                    "Estimación orientativa. La reserva de mantenimiento no es un gasto pagado. " +
                    "La recomendación no acepta ni rechaza viajes por ti.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}
