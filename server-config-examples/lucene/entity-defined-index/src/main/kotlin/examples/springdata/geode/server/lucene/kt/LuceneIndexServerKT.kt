package examples.springdata.geode.server.lucene.kt

import examples.springdata.geode.server.lucene.kt.config.EntityDefinedLuceneIndexServerConfigKT
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication(scanBasePackageClasses = [EntityDefinedLuceneIndexServerConfigKT::class])
class LuceneIndexServerKT {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplicationBuilder(LuceneIndexServerKT::class.java).web(WebApplicationType.NONE).build().run(*args)
        }
    }
}