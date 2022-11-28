package by.bulba.watch.elektronika.renderer.impl

internal sealed class RendererDrawerType {
    object Label : RendererDrawerType()
    object Background : RendererDrawerType()
    object Battery : RendererDrawerType()
    object DigitalClock : RendererDrawerType()
    object CenterRect : RendererDrawerType()
    object BottomLabel : RendererDrawerType()
}