package com.alfred.playerskins.render;

import net.minecraft.client.model.Quad;
import net.minecraft.client.model.Vertex;

public class PlayerQuad extends Quad {
    public PlayerQuad(Vertex[] vertices, int u1, int v1, int u2, int v2, boolean isClassic) {
        super(vertices);
        float yNorm = isClassic ? 32 : 64;

        float var6 = 0.0015625f;
        float var7 = 0.003125f;
        vertices[0] = vertices[0].remap(u2 / 64f - var6, v1 / yNorm + var7);
        vertices[1] = vertices[1].remap(u1 / 64f + var6, v1 / yNorm + var7);
        vertices[2] = vertices[2].remap(u1 / 64f + var6, v2 / yNorm - var7);
        vertices[3] = vertices[3].remap(u2 / 64f - var6, v2 / yNorm - var7);
    }
}
