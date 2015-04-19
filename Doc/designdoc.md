#Design doc

Controller: Main 

![](https://github.com/BartvonMeijenfeldt/Ghost/blob/master/Sketches/Start.jpg)

/PLAY

Controller: ChoosePlayers

![](https://github.com/BartvonMeijenfeldt/Ghost/blob/master/Sketches/ChoosePlayer.jpg)

/PLAY/NEW PLAYER

Controller: CreateNewAccount

![](https://github.com/BartvonMeijenfeldt/Ghost/blob/master/Sketches/CreateAccount.jpg)

/PLAY/EXISTING PLAYER

Controller: ChooseExistingPlayer

![](https://github.com/BartvonMeijenfeldt/Ghost/blob/master/Sketches/Existing%20players.jpg)

Listview to scroll through players

/PLAY/GAME STARTED

Controller: Game

![](https://github.com/BartvonMeijenfeldt/Ghost/blob/master/Sketches/Game.jpg)

with pop-up menu:

![](https://github.com/BartvonMeijenfeldt/Ghost/blob/master/Sketches/Menu.jpg)

Menu allows to restart the game of choose another language (and restart the game)

/PLAY/GAME STARTED/GAME ENDED

Controller: Game_Ended

![](https://github.com/BartvonMeijenfeldt/Ghost/blob/master/Sketches/Won.jpg)

/PLAY/GAME STARTED/GAME ENDED/HIGH SCORE

Controller: HighScore

![](https://github.com/BartvonMeijenfeldt/Ghost/blob/master/Sketches/Hi-ScoresAfterGame.jpg)

Listview to show players, but probably only the top players, if someone wants to see all hi-score he/she should go to high-scores from main

/SETTINGS

Controller: Settings

![](https://github.com/BartvonMeijenfeldt/Ghost/blob/master/Sketches/Settings.jpg)

/HIGH SCORE

Controller: HighScore

(Same controller as the other highscore screen, but by using what the previous screen was, it won't be a problem to use one controller for two screens)

![](https://github.com/BartvonMeijenfeldt/Ghost/blob/master/Sketches/Hi-Scores.jpg)

Listview to show all players in the High Score.


Classes
	with methods

Dictionary (with a string as parameter)
   filter
       this method takes a string as input and filters the word list using this string. Because loading the 	             dictionary takes quite a bit of time, this method should not destroy the base dictionary and thus 			       allows it to be re-used.
		
   count
      this method returns the length of the words remaining in the filtered list.

   result
       this method returns the single remaining word in the list. Obviously, this method can only be called 		       if count returns the number 1.

   reset
       to remove the filter and re-start with the original dictionary.
		
Game(with a Dictionary as parameter)
   guess
       this methods takes a string as input, representing the ltter that the current player has guessed. It 		       uses the Dictionary instance to decide.
		
   turn
       this method returns a boolean indicating which player is up for guessing.
	
   ended
       this method returns a boolean indicating if the game has ended.
		
   winner
       this method returns a boolean indicating which player has won the game. This method can obviously only 		       make sense if ended return true.
		
