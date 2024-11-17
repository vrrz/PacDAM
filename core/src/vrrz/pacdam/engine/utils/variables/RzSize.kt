package vrrz.pacdam.engine.utils.variables

import com.badlogic.gdx.Gdx

enum class RzSize(val width: Float, val height: Float) {
    ALL(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat()),
    X_LARGE(Gdx.graphics.width * 3 / 4F, Gdx.graphics.width * 3 / 4F),
    LARGE(Gdx.graphics.width * 2 / 3F, Gdx.graphics.height * 2 / 3F),
    MEDIUM(Gdx.graphics.width / 2F, Gdx.graphics.height / 2F),
    SMALL(Gdx.graphics.width / 3F, Gdx.graphics.height / 3F),
    X_SMALL(Gdx.graphics.width / 4F, Gdx.graphics.height / 4F),
    XX_SMALL(Gdx.graphics.width / 5F, Gdx.graphics.height / 5F),
    XXX_SMALL(Gdx.graphics.width / 6F, Gdx.graphics.height / 6F),
    CELL(Gdx.graphics.width / 34F, Gdx.graphics.width / 34F),
}