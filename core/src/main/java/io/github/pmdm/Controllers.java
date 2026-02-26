package io.github.pmdm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class Controllers {
    Viewport viewport;
    Stage stage;
    private boolean avanzar;
    private boolean retroceder;
    private boolean atacar;
    private boolean saltar;
    OrthographicCamera camera;
    //Movimiento
    Table tableIzquierda;
    //Salto y ataque
    Table tableDerecha;

    public Controllers() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(800,480, camera);
        stage = new Stage(viewport, Main.batch);
        Gdx.input.setInputProcessor(stage);

        tableIzquierda = new Table();
        tableIzquierda.setFillParent(true);
        tableIzquierda.left().bottom();

        tableDerecha = new Table();
        tableDerecha.setFillParent(true);
        tableDerecha.right().bottom();

        Image btnSaltar = new Image(new Texture("flecha-arriba.png"));
        btnSaltar.setSize(50,50);

        btnSaltar.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                setSaltar(true);
                return true;
            }

        });

        Image btnAtacar = new Image(new Texture("flecha-abajo.png"));
        btnAtacar.setSize(50,50);

        btnAtacar.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                setAtacar(true);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                setAtacar(false);
            }
        });

        Image btnAvanzar = new Image(new Texture("flecha-avanzar.png"));
        btnAvanzar.setSize(50,50);

        btnAvanzar.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                setAvanzar(true);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                setAvanzar(false);
            }
        });

        Image btnRetroceder = new Image(new Texture("flecha-retroceder.png"));
        btnRetroceder.setSize(50,50);

        btnRetroceder.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                setRetroceder(true);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                setRetroceder(false);
            }
        });


        tableIzquierda.add();
        tableIzquierda.row().pad(5,5,5,5);
        tableIzquierda.add(btnRetroceder).size(btnRetroceder.getWidth(), btnRetroceder.getHeight());
        tableIzquierda.add();
        tableIzquierda.add(btnAvanzar).size(btnAvanzar.getWidth(), btnAvanzar.getHeight());

        stage.addActor(tableIzquierda);

        tableDerecha.add();
        tableDerecha.row().pad(5,5,5,5);
        tableDerecha.add(btnAtacar).size(btnAtacar.getWidth(), btnAtacar.getHeight());
        tableDerecha.add();
        tableDerecha.add(btnSaltar).size(btnSaltar.getWidth(), btnSaltar.getHeight());

        stage.addActor(tableDerecha);


    }

    public void draw() {
        stage.draw();
    }

    public boolean isAvanzar() {
        return avanzar;
    }

    public void setAvanzar(boolean avanzar) {
        this.avanzar = avanzar;
    }

    public boolean isRetroceder() {
        return retroceder;
    }

    public void setRetroceder(boolean retroceder) {
        this.retroceder = retroceder;
    }

    public boolean isSaltar() {
        if (saltar){
            saltar=false;
            return true;
        }
        return false;
    }

    public void setSaltar(boolean saltar) {
        this.saltar = saltar;
    }

    public void resize(int wisth, int height) {
        viewport.update(wisth, height);
    }

    public boolean isAtacar() {
        return atacar;
    }

    public void setAtacar(boolean atacar) {
        this.atacar = atacar;
    }
}
