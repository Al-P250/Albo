package io.github.pmdm;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    ShapeRenderer shapeRenderer;
    public static SpriteBatch batch;
    private Texture background;
    Mob esqueleto;
    Personaje prota;
    Controllers controllers;
    OrthographicCamera camara;
    World world;
    Plataformas plataforma;
    boolean golpeRealizado = false;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        world = new World(new Vector2(0,-10), true);
        background = new Texture(Gdx.files.internal("NonParallax.png"));

        esqueleto = new Mob(300,100,"SkeletonWalk.png", 13);
        esqueleto.setVelocity(50,0);

        prota=new Personaje(100, 100);

        plataforma = new Plataformas(400,20,50,100, "bucket.png");

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
        boolean saltar = controllers.isSaltar() || ( Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.SPACE));
        boolean atacar = controllers.isAtacar() || Gdx.input.isKeyJustPressed(Input.Keys.W);

        if ((avanzar && !(prota.getBounds().overlaps(plataforma.getBounds())))||( (avanzar && prota.getPosition().y >= plataforma.getBounds().y+plataforma.getBounds().height -10 && prota.getPosition().y <= plataforma.getBounds().y+plataforma.getBounds().height +10))) {
            velocidad.x = 500;
        } else if ((retroceder && !prota.getBounds().overlaps(plataforma.getBounds()))||((retroceder && prota.getPosition().y >= plataforma.getBounds().y+plataforma.getBounds().height -10 && prota.getPosition().y <= plataforma.getBounds().y+plataforma.getBounds().height +10))) {
            velocidad.x = -500;
        }else {
            velocidad.x = 0;
        }
        if (saltar) {
            prota.jump();
        }
        if (atacar) {
            prota.attack();
        }

        prota.setVelocidad(velocidad);
    }

    int vidaMob = 1;
    public void checkAttack() {
        if (prota.isAttacking()) {
            if (!golpeRealizado && prota.getAttackBox().overlaps(esqueleto.getBounds())) {
                vidaMob--;
                golpeRealizado = true;

                if (vidaMob <= 0) {
                    esqueleto.setDead(true);
                }
            }
        } else {
            golpeRealizado = false;
        }
    }

    @Override
    public void render() {

        float deltaTime = Gdx.graphics.getDeltaTime();

        handleInput();

        world.step(1/60f,6,2);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Array<Rectangle> colisiones = new Array<>();
        colisiones.add(plataforma.getBounds());

        colisiones.add(new Rectangle(0, 0, 2000, 50));

        prota.update(deltaTime, colisiones);

        checkAttack();

        esqueleto.update(deltaTime, prota.position);

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
        plataforma.draw(batch);
        batch.end();

        shapeRenderer.setProjectionMatrix(camara.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        shapeRenderer.setColor(0,1,0,1); // plataforma
        shapeRenderer.rect(
            plataforma.getBounds().x,
            plataforma.getBounds().y,
            plataforma.getBounds().width,
            plataforma.getBounds().height
        );

        shapeRenderer.setColor(0,1,0,1); // personaje
        shapeRenderer.rect(
            prota.getBounds().x,
            prota.getBounds().y,
            prota.getBounds().width,
            prota.getBounds().height
        );

        shapeRenderer.setColor(1,0,0,1); // ataque
        shapeRenderer.rect(
            prota.getAttackBox().x,
            prota.getAttackBox().y,
            prota.getAttackBox().width,
            prota.getAttackBox().height
        );

        shapeRenderer.setColor(0,0,1,1); // enemigo
        shapeRenderer.rect(
            esqueleto.getBounds().x,
            esqueleto.getBounds().y,
            esqueleto.getBounds().width,
            esqueleto.getBounds().height
        );

        shapeRenderer.end();
        controllers.stage.act(deltaTime);
        controllers.draw();

    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        esqueleto.dispose();
        prota.dispose();
        plataforma.dispose();
        shapeRenderer.dispose();
    }
}
