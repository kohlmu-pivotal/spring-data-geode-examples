/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package examples.springdata.geode.client.security.kt.shiro.realm.support

import org.apache.shiro.codec.CodecSupport
import org.springframework.core.convert.converter.Converter

/**
 * The [ObjectToByteArrayConverter] class is a Spring [Converter] extending Apache Shiro's
 * [CodecSupport] to convert [Objects][Object] to a `byte[]`.
 *
 * @author John Blum
 * @author Udo Kohlmeyer
 *
 * @see CodecSupport
 *
 * @see org.springframework.core.convert.converter.Converter
 *
 * @since 1.0.0
 */
class ObjectToByteArrayConverter : CodecSupport(), Converter<Any, ByteArray> {

    /**
     * @inheritDoc
     */
    override fun convert(source: Any): ByteArray? = toBytes(source)

    companion object {

        val INSTANCE = ObjectToByteArrayConverter()
    }
}
