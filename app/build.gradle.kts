import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.internal.dsl.BuildType
import com.android.builder.core.DefaultApiVersion
import com.android.builder.core.DefaultProductFlavor
import com.android.builder.model.ApiVersion
import org.jetbrains.kotlin.gradle.plugin.KotlinAndroidPluginWrapper

apply {
    plugin<AppPlugin>()
    plugin<KotlinAndroidPluginWrapper>()
}

android {
    compileSdkVersion(25)
    buildToolsVersion("25.0.3")
    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(25)

        applicationId = "net.aquadc.advancedkotlinpatterns"
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles("proguard-rules.pro")
        }
        getByName("release") {
            // unused, I don't want to specify a signingCongfig
        }
    }
    packagingOptions {
        exclude("META-INF/*kotlin*")
        exclude("META-INF/kotlin*")
        exclude("kotlin")
        exclude("kotlin/*")
        exclude("kotlin/**")
    }
}

dependencies {
    /*androidTestCompile('com.android.support.test.espresso:espresso-core:2.2.2', {
        exclude group: 'com.android.support', module: 'support-annotations'
    })
    testCompile 'junit:junit:4.12'*/

    val ext = rootProject.extra

    compile("com.android.support:recyclerview-v7:${ext["support_version"]}")

    compile("org.jetbrains.kotlin:kotlin-stdlib-jre7:${ext["kotlin_version"]}")
    compile("org.jetbrains.anko:anko-sdk25:${ext["anko_version"]}")
    compile("org.jetbrains.anko:anko-recyclerview-v7:${ext["anko_version"]}")

    compile("com.squareup.picasso:picasso:2.5.2")
    compile("com.jakewharton.picasso:picasso2-okhttp3-downloader:1.1.0")
}

repositories {
    maven { url = uri("http://dl.bintray.com/kotlin/kotlin-eap-1.2") }
}

fun Project.android(setup: AppExtension.() -> Unit) = the<AppExtension>().setup()

fun NamedDomainObjectContainer<BuildType>.release(setup: BuildType.() -> Unit) = findByName("release").setup()

fun AppExtension.defaultConfigExtension(setup: DefaultProductFlavor.() -> Unit) = defaultConfig.setup()

fun AppExtension.buildTypesExtension(setup: NamedDomainObjectContainer<BuildType>.() -> Unit) = buildTypes({ setup() })

fun DefaultProductFlavor.setMinSdkVersion(value: Int) = setMinSdkVersion(value.asApiVersion())

fun DefaultProductFlavor.setTargetSdkVersion(value: Int) = setTargetSdkVersion(value.asApiVersion())

fun Int.asApiVersion(): ApiVersion = DefaultApiVersion.create(this)