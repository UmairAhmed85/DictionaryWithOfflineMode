package com.example.dictionarywithofflinemode

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dictionarywithofflinemode.feature_dictionary.presentation.WordInfoItem
import com.example.dictionarywithofflinemode.feature_dictionary.presentation.WordInfoViewmodel
import com.example.dictionarywithofflinemode.ui.theme.DictionaryWithOfflineModeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DictionaryWithOfflineModeTheme {
                val viewModel: WordInfoViewmodel = hiltViewModel()
                val state = viewModel.wordInfoState.value
                val scaffoldState = rememberScaffoldState()
                LaunchedEffect(key1 = true) {

                    viewModel.eventFlow.collectLatest { event ->
                        when (event) {
                            is WordInfoViewmodel.UIEvent.ShowSnackbar -> {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    message = event.message
                                )
                            }
                        }
                    }
                }
                Scaffold(
                    scaffoldState = scaffoldState
                ) {
                    Box(modifier = Modifier.background(MaterialTheme.colors.background)) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            TextField(
                                value = viewModel.searchQuery.value,
                                onValueChange = viewModel::onSearch,
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = {
                                    Text(text = "Search your word")
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                            ) {
                                items(state.wordInfoItem.size) { index ->
                                    val wordInfo = state.wordInfoItem[index]
                                    if (index > 0) {
                                        Spacer(modifier = Modifier.height(10.dp))
                                    }
                                    WordInfoItem(wordInfo = wordInfo)
                                    if (index < state.wordInfoItem.size - 1) {
                                        Divider()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}