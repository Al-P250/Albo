package io.github.pmdm;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Personaje{
    private Animation<TextureRegion> walkAnimation, jumpAnimation, attackAnimation;
    Texture jumpImg, protaImg, attackIMg;
    Sprite protaSprite;
    private Vector2 position, velocidad;
    private float stateTime, gravedad;
    private final float FRAME_DURATION = 0.2f;
    boolean suelo, isJumping;

    //ATAQUES
    boolean isAttacking = false;
    float attackTimer = 0;
    final float ATTACK_DURATION = 0.2f;
    Rectangle bounds;
    private Rectangle hurtBox, attackBox;
    boolean facingRight = true;

    int saltos=0;
    int numSaltos=2;

    public Personaje(float inicioX, float inicioY, int frameCount){
        position=new Vector2();
        velocidad = new Vector2();

        hurtBox = new Rectangle(inicioX, inicioY, 120, 140);
        attackBox = new Rectangle();

        protaImg = new Texture("Idle_KG_2.png");
        int walkFrameWidth = protaImg.getWidth() / frameCount;
        int walkFrameHeight = protaImg.getHeight();

        TextureRegion[][] tmpWalk = TextureRegion.split(protaImg,walkFrameWidth,walkFrameHeight);

        walkAnimation = new Animation<>(FRAME_DURATION,tmpWalk[0]);
        walkAnimation.setPlayMode(Animation.PlayMode.LOOP);

        protaSprite = new Sprite(protaImg);

        protaSprite.setPosition(inicioX,inicioY);
        getPosition().set(inicioX,inicioY);
        protaSprite.setPosition(inicioX,inicioY);
        suelo=true;
        setGravedad(1000f);
        protaSprite.setSize(160,160);

        jumpImg = new Texture("Jump_KG_2.png");
        int jumpFrameWidth = protaImg.getWidth() / frameCount;
        int jumpFrameHeight = protaImg.getHeight();

        TextureRegion[][] tmpJump = TextureRegion.split(jumpImg,jumpFrameWidth,jumpFrameHeight);

        jumpAnimation = new Animation<>(FRAME_DURATION,tmpJump[0]);
        jumpAnimation.setPlayMode(Animation.PlayMode.NORMAL);

        attackIMg = new Texture("Attack_KG_2.png");
        int attackFrameWidth = attackIMg.getWidth() / 6;
        int attackFrameHeight = attackIMg.getHeight();

        TextureRegion[][] tmpAttack = TextureRegion.split(attackIMg,attackFrameWidth,attackFrameHeight);

        attackAnimation = new Animation<>(FRAME_DURATION,tmpAttack[0]);
        attackAnimation.setPlayMode(Animation.PlayMode.NORMAL);

        bounds = new Rectangle(inicioX, inicioY, 100,protaSprite.getHeight());
        bounds.setPosition(position.x, position.y);

    }

    public void jump() {
        //se podría cambiar a velocidad.y > 0 para evitar problemas en un futuro
        if (saltos < numSaltos){
            getVelocidad().y = 500f;
            isJumping = true;
            stateTime = 0;
            suelo = false;
            saltos++;
        }
    }
    public void attack() {
        if (!isAttacking) {
            isAttacking = true;
            attackTimer = ATTACK_DURATION;
        }
    }

    public void update(float delta, Rectangle plataforma){
        stateTime += delta;

        getVelocidad().y -= getGravedad() * delta;
        getPosition().x += getVelocidad().x*delta;
        getPosition().y += getVelocidad().y*delta;

        position.x = MathUtils.clamp(position.x, 0, Gdx.graphics.getWidth() - protaSprite.getWidth());
        position.y = MathUtils.clamp(position.y, 0, Gdx.graphics.getHeight() - protaSprite.getHeight());

        if (getPosition().y <= 0) {
            getPosition().y = 0;
            getVelocidad().y = 0;
            suelo = true;
            stateTime = 0;
            saltos=0;
        }

        TextureRegion currentFrame;

        hurtBox.setPosition(position.x, position.y);

        if (!suelo) {
            currentFrame = jumpAnimation.getKeyFrame(stateTime);
        } else {
            currentFrame = walkAnimation.getKeyFrame(stateTime);
        }
        if (isAttacking) {
            currentFrame = attackAnimation.getKeyFrame(stateTime);

            float attackWidth = 80;
            float attackHeight = 120;

            if (facingRight) {
                attackBox.set(position.x + hurtBox.width,
                    position.y,
                    attackWidth,
                    attackHeight);
            } else {
                attackBox.set(position.x - attackWidth,
                    position.y,
                    attackWidth,
                    attackHeight);
            }

        } else {
            attackBox.set(0,0,0,0);
        }
        if (velocidad.x > 0) facingRight = true;
        if (velocidad.x < 0) facingRight = false;

        protaSprite.setRegion(currentFrame);
        protaSprite.setPosition(position.x, position.y);
        bounds.setPosition(position.x, position.y);

        if (getVelocidad().y <= 0) {
            float protaBottom = getPosition().y;
            float plataformaTop = plataforma.y + plataforma.height;

            if (protaBottom >= plataformaTop -10 && protaBottom <= plataformaTop + 10 &&
                getPosition().x + protaSprite.getWidth() >= plataforma.x &&
                getPosition().x <= plataforma.x + plataforma.width) {
                protaSprite.setPosition(getPosition().x, plataformaTop);
                getPosition().y = plataformaTop;
                getVelocidad().y = 0;
                suelo = true;
                isJumping = false;
                saltos = 0;
            }
        }

    }
    public Rectangle getAttackBox() {
        return attackBox;
    }
    public Rectangle getBounds() {
        return bounds;
    }
    public void draw(SpriteBatch batch) {
        protaSprite.draw(batch);
    }
    public void dispose(){
        protaImg.dispose();
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(Vector2 velocidad) {
        this.velocidad = velocidad;
    }

    public float getGravedad() {
        return gravedad;
    }

    public void setGravedad(float gravedad) {
        this.gravedad = gravedad;
    }
}
