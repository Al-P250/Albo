package io.github.pmdm;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    public static SpriteBatch batch;
    private Texture background;
    Texture texturePlataforma;
    Mob esqueleto;
    Personaje prota;
    Controllers controllers;
    OrthographicCamera camara;
    World world;

    Rectangle plataforma;

    @Override
    public void create() {
        batch = new SpriteBatch();

        world = new World(new Vector2(0,-10), true);
        background = new Texture(Gdx.files.internal("NonParallax.png"));

        esqueleto = new Mob(100,100,"SkeletonWalk.png", 13);
        esqueleto.setVelocity(50,0);

        prota=new Personaje(100, 100,4);

        plataforma = new Rectangle(400,20,30,100);
        texturePlataforma = new Texture("bucket.png");

        controllers = new Controllers();

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        camara=new OrthographicCamera();
        camara.setToOrtho(false,1000,480);
        controllers.resize(width, height);
    }

    public void handleInput() {
        Vector2 velocidad = prota.getVelocidad();
        boolean avanzar = controllers.isAvanzar() || Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        boolean retroceder = controllers.isRetroceder() || Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean saltar = controllers.isSaltar() || ( Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE));


        if ((avanzar && !(prota.getPosition().x + prota.protaSprite.getWidth() >= plataforma.x ))||((avanzar && prota.getPosition().y >= plataforma.y+plataforma.height -10 && prota.getPosition().y <= plataforma.y+plataforma.height +10))) {
            velocidad.x = 500;
        } else if ((retroceder && !(prota.getPosition().x <= plataforma.x + plataforma.width))||((retroceder && prota.getPosition().y >= plataforma.y+plataforma.height -10 && prota.getPosition().y <= plataforma.y+plataforma.height +10))) {
            velocidad.x = -500;
        }else {
            velocidad.x = 0;
        }
        if (saltar) {
            prota.jump();
        }



        prota.setVelocidad(velocidad);
    }

    @Override
    public void render() {

        float deltaTime = Gdx.graphics.getDeltaTime();

        handleInput();

        world.step(1/60f,6,2);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        prota.update(deltaTime, plataforma);


        esqueleto.update(deltaTime);

        camara.position.x +=(prota.getPosition().x -camara.position.x)*0.1f;
        camara.position.y +=(prota.getPosition().y -camara.position.y)*0.1f;
        camara.position.x= MathUtils.clamp(prota.getPosition().x,camara.viewportWidth/2, Gdx.graphics.getWidth()-camara.viewportWidth/2);
        camara.position.y= MathUtils.clamp(prota.getPosition().y,camara.viewportHeight/2, Gdx.graphics.getHeight()-camara.viewportHeight/2);
        camara.update();

        batch.setProjectionMatrix(camara.combined);
        batch.begin();

        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        esqueleto.draw(batch);
        prota.draw(batch);
        batch.draw(texturePlataforma, plataforma.x+5, plataforma.y, plataforma.width+80, plataforma.height);
        batch.end();

        controllers.stage.act(deltaTime);
        controllers.draw();

    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        esqueleto.dispose();
        prota.dispose();
    }
}
