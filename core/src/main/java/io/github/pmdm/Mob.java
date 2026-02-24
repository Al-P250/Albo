package io.github.pmdm;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Mob extends Entidad {

    public Mob(float x, float y, String spriteSheetPath, int frameCount) {
        super(spriteSheetPath, frameCount, 0.1f);
        sprite.setPosition(x, y);
    }

    @Override
    public void update(float deltaTime){
        position.add(velocidad.cpy().scl(deltaTime));

    }
    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
        sprite.setSize(160, 320);
    }
}
