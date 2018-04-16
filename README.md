
## Code Structure
>
> + **Configuration (Interface) :** All global constant such as the size of board (7), the length of winning chain (5).
> + **Agent (Abstract Class) :** public attributes of agents and an abstract method called action.
> + **State Class :** The class that defined state.
> + **Agent Classes [ReflexAgent, MiniMax (with MiniMax Agent, Alpha-beta Agent and Stochastic Agent in it), SupervisedAgent] :** Classes for specific agent, extent agent class.
> + **UI Class [BoardUI, humanListener, GamePanel] :** Classes for GUI.
> + **Main Class [GomokuMain, PlayGame] :** Classes that instruct with agents to play.
> + **RegressionTraining Class :** Generates data for linear regression.

## Minimax and Alpha-beta Agents
Implemented both minimax search and alpha-beta search agents. Designed an evaluation function that is accurate enough to permit minimax and alpha-beta agents to beat the reflex agent. 

## Stochastic search
Implemented a Stochastic-search agent for the 7x7 Gomoku game. 

## supervised learning
Used supervised learning to estimate an evaluation function: 

 + **Featurize:** 
 We randomly generate 5000 games as the training data. For each game, we use a 10 dimension vector to describe it’s feature. Remember that in part 2.0.3, we introduce our pattern recognition mechanism. Therefore, for vector (X1, X2, ...X10), X1 means the number of valid patterns that contains five ‘x’, X2 means the number of valid patterns that contains five ‘o’, X3 means the number of valid patterns that contains four ‘x’, X4 means the number of valid patterns that contains four ‘o’, respectively.

 + **Value:** 
For each training data point, we will calculate a value as the percentage of winning from this state. Here we reuse the code in part 2.3, randomly generate 500 games from this starting state and return the winning percentage * 10000 as it’s value. The generated data are stored in five txt files which are uploaded with our code. 

 + **Linear regression:** 
We use linear regression to find the coefficient of the variables, we have generated three datasets which contains 100, 1000, 5000 rows of data. And we choose to use the result of 5000 data because we believe that more data means more accuracy. 
We use RStudio as software to process our dataset. First, we load the dataset and generate a preview of the table. Then we can see variables X1 to X11
The formula is yi = 0 + 1xi1 + 2xi2 + ... pxip + i for i = 1,2, ... n.
Then we use RStudio built-in function “lm” which is used to process multiple linear regression. 


## Preview
![Preview](https://raw.githubusercontent.com/ForestCold/Images/master/Screen%20Shot%202018-03-11%20at%201.15.13%20AM.png)
