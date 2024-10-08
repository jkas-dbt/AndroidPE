-printconfiguration ~/tmp/full-r8-config.txt
-printusage ~/tmp/full-r8-usage.txt
-dontwarnings
-dontshrink

-keepclassmembers class fqcn.of.javascript.interface.for.webview {
 public *;
}

-dontwarn java.beans.BeanInfo
-dontwarn java.beans.FeatureDescriptor
-dontwarn java.beans.IntrospectionException
-dontwarn java.beans.Introspector
-dontwarn java.beans.PropertyDescriptor
-dontwarn javax.lang.model.element.Modifier

-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

-dontwarn androidx.appcompat.graphics.drawable.DrawableWrapper
-dontwarn androidx.databinding.DataBinderMapperImpl
-dontwarn androidx.databinding.DataBindingComponent
-dontwarn androidx.window.extensions.WindowExtensions
-dontwarn androidx.window.extensions.WindowExtensionsProvider
-dontwarn androidx.window.extensions.embedding.ActivityEmbeddingComponent
-dontwarn androidx.window.extensions.embedding.ActivityRule$Builder
-dontwarn androidx.window.extensions.embedding.ActivityRule
-dontwarn androidx.window.extensions.embedding.ActivityStack
-dontwarn androidx.window.extensions.embedding.EmbeddingRule
-dontwarn androidx.window.extensions.embedding.SplitInfo
-dontwarn androidx.window.extensions.embedding.SplitPairRule$Builder
-dontwarn androidx.window.extensions.embedding.SplitPairRule
-dontwarn androidx.window.extensions.embedding.SplitPlaceholderRule$Builder
-dontwarn androidx.window.extensions.embedding.SplitPlaceholderRule
-dontwarn androidx.window.extensions.layout.DisplayFeature
-dontwarn androidx.window.extensions.layout.FoldingFeature
-dontwarn androidx.window.extensions.layout.WindowLayoutComponent
-dontwarn androidx.window.extensions.layout.WindowLayoutInfo
-dontwarn androidx.window.sidecar.SidecarDeviceState
-dontwarn androidx.window.sidecar.SidecarDisplayFeature
-dontwarn androidx.window.sidecar.SidecarInterface$SidecarCallback
-dontwarn androidx.window.sidecar.SidecarInterface
-dontwarn androidx.window.sidecar.SidecarProvider
-dontwarn androidx.window.sidecar.SidecarWindowLayoutInfo
-dontwarn java.lang.reflect.AnnotatedType
-dontwarn javax.lang.model.element.Modifier
