package io.meltec.prima.client.model

import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry

object PrimaModels {
  fun register() {
    ModelLoadingRegistry.INSTANCE.registerResourceProvider(::PrimaObjModelResourceProvider)
  }
}
