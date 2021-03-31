package info.plateaukao.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import info.plateaukao.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    //AnimateAsStateDemo()
                    UpdateTransitionDemo()
                }
            }
        }
    }
}

@Composable
fun AnimateAsStateDemo() {
    var blue by remember  { mutableStateOf(true) }
    val color by animateColorAsState(
        targetValue = if (blue) Blue else Green,
    )

    Column {
        Button (
            onClick = { blue = !blue}
        ) {
            Text("Change color")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Box (
            modifier = Modifier
                .size(128.dp)
                .background(color = color)
        )
    }
}

private enum class BoxState {
    Small, Large
}
@Composable
fun UpdateTransitionDemo() { // two properties to be changed
    var boxState by remember { mutableStateOf(BoxState.Small) }
    val transition = updateTransition(targetState = boxState)
    val color by transition.animateColor { state ->
        when (state) {
            BoxState.Small -> Blue
            BoxState.Large -> Green
        }
    }
    val size by transition.animateDp { state ->
        when (state) {
            BoxState.Small -> 64.dp
            BoxState.Large -> 128.dp
        }
    }

    Column {
        Button(
            onClick = {
                boxState = when(boxState) {
                    BoxState.Small -> BoxState.Large
                    BoxState.Large -> BoxState.Small
                }
            }
        ) {
            Text("Change color and size")
        }

        Box(
            modifier = Modifier
                .size(size)
                .background(color = color)
        )
    }

}

@Composable
fun UpdateTransitionWithParametersDemo() { // change parameters
    var boxState by remember { mutableStateOf(BoxState.Small) }
    val transition = updateTransition(targetState = boxState)
    val color by transition.animateColor { state ->
        when (state) {
            BoxState.Small -> Blue
            BoxState.Large -> Green
        }
    }
    val size by transition.animateDp(
        transitionSpec = {
            if (targetState == BoxState.Large) {
                //weakSpring()
                tween(durationMillis = 500)
            } else {
                //mediumSpring()
                tween(durationMillis = 200)
            }
        }
    ) { state ->
        when (state) {
            BoxState.Small -> 64.dp
            BoxState.Large -> 128.dp
        }
    }

    Column {
        Button(
            onClick = {
                boxState = when(boxState) {
                    BoxState.Small -> BoxState.Large
                    BoxState.Large -> BoxState.Small
                }
            }
        ) {
            Text("Change color and size")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(size)
                .background(color = color)
        )
    }

}

@ExperimentalAnimationApi
@Composable
fun AnimatedVisibilityDemo() {
    var visible by remember { mutableStateOf(true) }

    Column {
        Button(
            onClick = { visible = !visible }
        ) {
            Text(text = if (visible) "Hide" else "Show")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // use animatedVisibility widget here!
        AnimatedVisibility(
            visible,
            enter = fadeIn(animationSpec = tween(durationMillis = 1000)),
            exit = fadeOut(animationSpec = tween(durationMillis = 1000)),
        ) {
            Box(
                modifier = Modifier
                    .size(128.dp)
                    .background(Blue)
            )
        }

    }
}

@Composable
fun AnimateContentSizeDemo() {
    var expanded by remember { mutableStateOf(true) }

    Column {
        Button(
            onClick = { expanded = !expanded }
        ) {
            Text(text = if (expanded) "Shrink" else "Expand")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            // add this line!
            modifier = Modifier
                .background(LightGray)
                .animateContentSize(animationSpec = tween(durationMillis = 1000))
        ) {
            Text(
                text = "888888888890012345670012345670012345670012345670012345670012345670012345670012345670012345670112345670234567898888888888900123456700123456700123456700123456700123456700123456700123456700123456700123456701123456702345678900888888888890012345670012345670012345670012345670012345670012345670012345670012345670012345670112345670234567890",
                fontSize = 16.sp,
                textAlign = TextAlign.Justify,
                modifier = Modifier.padding(16.dp),
                maxLines = if (expanded) Int.MAX_VALUE else 2
            )
        }
    }
}

private enum class DemoScene { Text, Icon }

@Composable
fun CrossFadeDemo() {
    var scene by remember { mutableStateOf(DemoScene.Text) }

    Column {
        Button(
            onClick = {
                scene = when (scene) {
                    DemoScene.Text -> DemoScene.Icon
                    DemoScene.Icon -> DemoScene.Text
                }
            }
        ) {
            Text(text = "Toggle")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Crossfade(
            targetState = scene,
            animationSpec = tween(durationMillis = 1000),
        ){ currentScene ->
            when (currentScene) {
                DemoScene.Text -> Text(
                    text = "Phone",
                    fontSize = 32.sp,
                )
                DemoScene.Icon -> Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Phone",
                    modifier = Modifier.height(48.dp)
                )
            }
        }
    }
}

@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
         Column {
            AnimateAsStateDemo()
            UpdateTransitionDemo()
            UpdateTransitionWithParametersDemo()
            AnimatedVisibilityDemo()
            AnimateContentSizeDemo()
            CrossFadeDemo()
        }
    }
}