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
    private boolean saltar;
    OrthographicCamera camera;

    public Controllers() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(800,480, camera);
        stage = new Stage(viewport, Main.batch);
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        table.right().bottom();

        Image btnSaltar = new Image(new Texture("flecha-arriba.png"));
        btnSaltar.setSize(50,50);

        btnSaltar.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                setSaltar(true);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                setSaltar(false);
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

        table.add();
        table.add(btnSaltar).size(btnSaltar.getWidth(), btnSaltar.getHeight());
        table.add();
        table.row().pad(5,5,5,5);
        table.add(btnRetroceder).size(btnRetroceder.getWidth(), btnRetroceder.getHeight());
        table.add();
        table.add(btnAvanzar).size(btnAvanzar.getWidth(), btnAvanzar.getHeight());

        stage.addActor(table);


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
        return saltar;
    }

    public void setSaltar(boolean saltar) {
        this.saltar = saltar;
    }

    public void resize(int wisth, int height) {
        viewport.update(wisth, height);
    }
}
