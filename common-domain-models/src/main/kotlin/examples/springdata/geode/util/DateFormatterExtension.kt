package examples.springdata.geode.util

import java.text.SimpleDateFormat
import java.util.*

fun Date.format(formatPattern: String) = SimpleDateFormat(formatPattern).format(this)