package com.sem.pool.scene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Rectangle;
import jdk.nashorn.internal.ir.Block;

import javax.swing.text.AttributeSet;

/**
 * Class representing a 3D Board of a single game.
 */
public class Table3D {
    private transient ModelInstance model;
    private transient ModelInstance[] rectangles = new ModelInstance[5];

    /**
     * Constructs a new 3D Board instance with the specified model.
     * @param model  Model object of the Board
     */
    public Table3D(ModelInstance model) {
        this.model = model;
        // initialize rectangles
        // Initialize floor
        ModelBuilder modelBuilder = new  ModelBuilder();
        ModelInstance rectangleE = new ModelInstance(modelBuilder.createBox(10f, 0.2f, 5f,
                new Material(ColorAttribute.createDiffuse(Color.WHITE)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal));
        this.rectangles[0] = rectangleE;
    }
    public ModelInstance getModel() {
        return model;
    }

    public ModelInstance getRectangleE() {
        return this.rectangles[0];
    }



}
