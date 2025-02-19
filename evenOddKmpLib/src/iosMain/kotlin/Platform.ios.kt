import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    
    val isEven  = isEven(3)

    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()