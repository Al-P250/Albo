package io.github.pmdm;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
    private Texture background;

    Mob esqueleto;

    Personaje prota;


    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("NonParallax.png"));
        esqueleto = new Mob(100,100,"SkeletonWalk.png", 13);
        esqueleto.setVelocity(50,0);
        prota=new Personaje("bucket.png", 100, 100);
        //image = new Texture("libgdx.png");
    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        esqueleto.update(deltaTime);
        prota.update(deltaTime);
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        esqueleto.draw(batch);
        prota.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        esqueleto.dispose();
        prota.dispose();
        //image.dispose();
    }
}
