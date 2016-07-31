package com.monyetmabuk.rajawali.tutorials.graphicengine.graphics;

import org.rajawali3d.Geometry3D;
import org.rajawali3d.Object3D;

/**
 * Created by CurTro Studios on 7/28/2016.
 */

public class SimplePlane extends Object3D {
    protected int mDirection;
    protected float mHeight;
    protected int mSegmentsH;
    protected int mSegmentsW;
    protected float mWidth;

    public SimplePlane() {
        this(1.0f, 1.0f, 0, 3, 3);
    }

    public SimplePlane(float width, float height, int segmentsW, int segmentsH) {
        this(width, height, 1, segmentsW, segmentsH);
    }

    public SimplePlane(float width, float height, int direction, int segmentsW, int segmentsH) {
        this.mWidth = width;
        this.mHeight = height;
        this.mSegmentsW = segmentsW;
        this.mSegmentsH = segmentsH;
        this.mDirection = direction;
        init();
    }

    public void recalculatePlane(float width, float height, int direction, int segmentsW, int segmentsH) {
        this.mWidth = width;
        this.mHeight = height;
        this.mSegmentsW = segmentsW;
        this.mSegmentsH = segmentsH;
        this.mDirection = direction;
        if (this.mGeometry != null) {
            this.mGeometry.destroy();
        }
        this.mGeometry = new Geometry3D();
        init();
    }

    private void init() {
        int numVertices = (this.mSegmentsW + 1) * (this.mSegmentsH + 1);
        float[] vertices = new float[(numVertices * 3)];
        float[] textureCoords = new float[(numVertices * 2)];
        float[] normals = new float[(numVertices * 3)];
        float[] colors = new float[(numVertices * 4)];
        int[] indices = new int[((this.mSegmentsW * this.mSegmentsH) * 6)];
        int vertexCount = 0;
        int texCoordCount = 0;
        for (int i = 0; i <= this.mSegmentsW; i++) {
            int j;
            for (j = 0; j <= this.mSegmentsH; j++) {
                vertices[vertexCount] = ((((float) i) / ((float) this.mSegmentsW)) - 0.5f) * this.mWidth;
                if (this.mDirection == 0) {
                    vertices[vertexCount + 1] = 0.0f;
                    vertices[vertexCount + 2] = ((((float) j) / ((float) this.mSegmentsH)) - 0.5f) * this.mHeight;
                } else {
                    vertices[vertexCount + 1] = ((((float) j) / ((float) this.mSegmentsH)) - 0.5f) * this.mHeight;
                    vertices[vertexCount + 2] = 0.0f;
                }
                int i2 = texCoordCount + 1;
                textureCoords[texCoordCount] = (float) (1 - i);
                if (j == 1) {
                    texCoordCount = i2 + 1;
                    textureCoords[i2] = 0.01f;
                } else {
                    texCoordCount = i2 + 1;
                    textureCoords[i2] = (float) (1 - j);
                }
                normals[vertexCount] = 0.0f;
                normals[vertexCount + 1] = 1.0f;
                normals[vertexCount + 2] = 0.0f;
                vertexCount += 3;
            }
        }
        int colspan = this.mSegmentsW + 1;
        int indexCount = 0;
        for (int row = 1; row <= this.mSegmentsH; row++) {
            for (int col = 1; col <= this.mSegmentsW; col++) {
                int lr = (row * colspan) + col;
                int ll = lr - 1;
                int ur = lr - colspan;
                int ul = ur - 1;
                int indexCount2 = indexCount + 1;
                indices[indexCount] = ul;
                indexCount = indexCount2 + 1;
                indices[indexCount2] = ur;
                indexCount2 = indexCount + 1;
                indices[indexCount] = lr;
                indexCount = indexCount2 + 1;
                indices[indexCount2] = ul;
                indexCount2 = indexCount + 1;
                indices[indexCount] = lr;
                indexCount = indexCount2 + 1;
                indices[indexCount2] = ll;
            }
        }
        int numColors = numVertices * 4;
        int j;
        for (j = 0; j < numColors; j += 4) {
            colors[j] = 1.0f;
            colors[j + 1] = 1.0f;
            colors[j + 2] = 1.0f;
            colors[j + 3] = 1.0f;
        }
        setData(vertices, normals, textureCoords, colors, indices,false);

    }

    public float getPlaneWidth() {
        return this.mWidth;
    }

    public float getPlaneHeight() {
        return this.mHeight;
    }

    public void setTransparent(boolean value) {
        this.mTransparent = value;
        this.mEnableBlending = value;
        setBlendFunc(770, 771);
        this.mEnableDepthMask = !value;
    }
}