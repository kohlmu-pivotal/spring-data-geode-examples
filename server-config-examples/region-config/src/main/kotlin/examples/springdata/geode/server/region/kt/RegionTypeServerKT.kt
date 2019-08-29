package examples.springdata.geode.server.region.kt

import examples.springdata.geode.server.region.kt.config.RegionTypeConfigurationKT
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication(scanBasePackageClasses = [RegionTypeConfigurationKT::class])
class RegionTypeServerKT {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplicationBuilder(RegionTypeServerKT::class.java)
                    .web(WebApplicationType.NONE)
                    .build()
                    .run(*args)
        }
    }
}