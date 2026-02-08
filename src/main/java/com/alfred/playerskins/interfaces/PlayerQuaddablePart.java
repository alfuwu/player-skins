package com.alfred.playerskins.interfaces;

import net.minecraft.client.model.ModelPart;

public interface PlayerQuaddablePart {
    ModelPart convertToPlayerQuads(boolean isClassic);

    void setUV(int u, int v);
}
