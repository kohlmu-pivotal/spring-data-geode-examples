package examples.springdata.geode.domain

import java.io.Serializable

data class OrderProductSummaryKey(val productId: Long?, val timebucketStart: Long) : Serializable