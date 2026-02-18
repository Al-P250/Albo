package io.github.pmdm;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture background;

    Mob esqueleto;

    Personaje prota;

    //Para clickar con ratón
    Vector2 touchPos;

    OrthographicCamera camara;


    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("NonParallax.png"));
        esqueleto = new Mob(100,100,"SkeletonWalk.png", 13);
        esqueleto.setVelocity(50,0);
        prota=new Personaje("bucket.png", 100, 100);
        camara=new OrthographicCamera();
        camara.setToOrtho(false,1000,480);
    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        prota.update(deltaTime);
        esqueleto.update(deltaTime);
        camara.position.x +=(prota.position.x -camara.position.x)*0.1f;
        camara.position.y +=(prota.position.y -camara.position.y)*0.1f;
        camara.position.x= MathUtils.clamp(prota.position.x,camara.viewportWidth/2, Gdx.graphics.getWidth()-camara.viewportWidth/2);
        camara.position.y= MathUtils.clamp(prota.position.y,camara.viewportHeight/2, Gdx.graphics.getHeight()-camara.viewportHeight/2);
        camara.update();
        batch.setProjectionMatrix(camara.combined);
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
    }
}
