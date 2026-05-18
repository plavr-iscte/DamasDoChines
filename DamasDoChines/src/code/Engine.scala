package code.code

import scala.annotation.tailrec
import scala.collection.parallel.CollectionConverters._

import gui.Controller
import code.code.Functions

object Engine {


	def play(board: Board,player: Stone,coordFrom: Coord2D,coordTo: Coord2D,lstOpenCoords: List[Coord2D]): (Option[Board], List[Coord2D]) = {
		def validJump: Boolean = {
			val difx = math.abs(coordTo.x - coordFrom.x)
			val dify = math.abs(coordTo.y - coordFrom.y)
			((difx == 2) && (dify == 0)) || ((difx == 0) && (dify == 2))
		}

		if board.get(coordFrom).contains(player) && validJump then {
			val coordMid = Coord2D((coordTo.x + coordFrom.x) / 2, (coordTo.y + coordFrom.y) / 2)
			if board.get(coordMid).contains(oppositeStone(player)) && lstOpenCoords.contains(coordTo) then {
				val newB = (board - coordMid - coordFrom) + (coordTo -> player)
				val newlst = coordMid :: coordFrom :: lstOpenCoords.filterNot(_ == coordTo).distinct
				(Some(newB), newlst)
			} else {
				(None, lstOpenCoords)
			}
		} else {
			(None, lstOpenCoords)
		}
	}

	def oppositeStone(player: Stone): Stone = 
		player match {
			case Stone.Black => Stone.White
			case Stone.White => Stone.Black
		}

	def initboard(x:Int,y:Int,r:MyRandom): (Board,MyRandom,List[Coord2D])={
		val (x_rem,ry) = r.nextInt(x)
		val (y_rem, rd) = ry.nextInt(y)
		val (dir, rfinal) = rd.nextInt(4)
		val c1_rem = Coord2D(x_rem,y_rem)
		val c2_rem = (dir,(x_rem,y_rem)) match {
			case (0,(0,_)) =>
				Coord2D(x_rem+1,y_rem)
			case (0,_) =>
				Coord2D(x_rem-1,y_rem)
			case(1,(xr,_)) if xr == x-1=>
				Coord2D(x_rem-1,y_rem)
			case (1,_) =>
				Coord2D(x_rem+1,y_rem)
			case(2,(_,0)) =>
				Coord2D(x_rem,y_rem+1)
			case (2,_) =>
				Coord2D(x_rem,y_rem-1)
			case(3,(_,yr)) if yr == y-1 =>
				Coord2D(x_rem,y_rem-1)
			case (3,_) =>
				Coord2D(x_rem,y_rem+1)
		}
		
		def putStone(
			c_rem1: Coord2D,
			c_rem2: Coord2D,
			col: Int,
			lin: Int
		):(List[(Coord2D,Stone)]) ={ //changed (..,..)
			val s = if (lin+col) % 2 == 0 then Stone.White else Stone.Black
			val c = Coord2D(lin ,col)

			val (new_lin,new_col) = if col == 0 then (lin-1,y-1) else (lin,col-1)

			if c == Coord2D(0,0) then 
				(c,s)::Nil
			else if c==c_rem1 || c==c_rem2 then 
				putStone(c_rem1, c_rem2, new_col, new_lin)
			else 
				(c,s)::putStone(c_rem1, c_rem2, new_col, new_lin)

		}

		val openCoords = List(c1_rem, c2_rem)
		val entries = putStone(c1_rem, c2_rem, y-1,x-1).toMap.par
		(entries.asInstanceOf[Board], rfinal.asInstanceOf[MyRandom], openCoords)

	}

	/*def playRandomly(board: Board, r: MyRandom, player: Stone, lstOpenCoords: List[Coord2D], coordFrom:Option[Coord2D] , f: (List[Coord2D], MyRandom) => (Coord2D, MyRandom)): (Option[Board], MyRandom, List[Coord2D], Option[Coord2D]) = {
    if !(coordFrom.isDefined) then
      if lstOpenCoords.isEmpty then { (None, r, lstOpenCoords, None) }
      else {
        val (target, rs) = f(lstOpenCoords, r)
        val pCoordinates = List(
          Coord2D(target.x - 2, target.y), Coord2D(target.x + 2, target.y), Coord2D(target.x, target.y - 2), Coord2D(target.x, target.y + 2)
        )
        val (fBoard, openCoords) = (pCoordinates foldLeft (None:Option[Board], lstOpenCoords)) {
          case ((None, _), coordFrom) =>
            play(board, player, coordFrom, target, lstOpenCoords)
          case (acc, _) =>
            acc
        }
        fBoard match {
          case Some(newBoard) =>
            /*val (dice, rsN) = rs.nextInt(2)
            val rsNn = rsN.asInstanceOf[MyRandom]
            if (dice == 0) {
              val (recBoard, recR, recOpen, recCoord) = playRandomly(newBoard, rsNn, player, openCoords, f)
              recBoard match {
                case Some(_) => (recBoard, recR, recOpen, recCoord)
                case None    => (Some(newBoard), rsNn, openCoords, Some(target))
              }
            } else {
              (Some(newBoard), rsNn, openCoords, Some(target))
            }*/
            (Some(newBoard), rs.asInstanceOf[MyRandom], openCoords, Some(target))
          case None =>
            playRandomly(board, rs, player, lstOpenCoords filter (x => x!=target), f)
        }
      }
    else
      val cf = Some(coordFrom).asInstanceOf[Coord2D]
      val listCoordsTo= moves(cf)
      val listplays = listCoordsTo.map(c => play(board, player, cf, c, lstOpenCoords)).filterNot((b,l) => b == None)
      
      if listplays.isEmpty then
        (None,r,lstOpenCoords,None)
      else
        val (rplay,rs)= f(listplays,r)
        ()
        
      
	}*/

	def playRandomly(
		board: Board,
		r: MyRandom,
		player: Stone,
		lstOpenCoords: List[Coord2D],
		forcedFrom: Option[Coord2D],
		f: (List[Coord2D], MyRandom) => (Coord2D, MyRandom)
	): (Option[Board], MyRandom, List[Coord2D], Option[Coord2D]) = {

		forcedFrom match {
			case Some(from) =>
				// Successive jump: only the same stone can move. 
				val validTargets = moves(from).filter { target =>
					play(board, player, from, target, lstOpenCoords)._1.nonEmpty
				}

				if (validTargets.isEmpty) (None, r, lstOpenCoords, None)
				else {
					val (target, nextR) = f(validTargets, r)
					val (newBoard, newOpen) = play(board, player, from, target, lstOpenCoords)
					(newBoard, nextR.asInstanceOf[MyRandom], newOpen, Some(target))
				}

			case None =>
				def tempFrom(tempOpenCoords: List[Coord2D], rand: MyRandom): (Option[Board], MyRandom, List[Coord2D], Option[Coord2D]) = {
					if tempOpenCoords.isEmpty then (None, rand, lstOpenCoords, None)
					else {
						val (target, nextR) = f(tempOpenCoords, rand)
						val tempCoords = List(
							Coord2D(target.x - 2, target.y),
							Coord2D(target.x + 2, target.y),
							Coord2D(target.x, target.y - 2),
							Coord2D(target.x, target.y + 2)
						)

						val maybeMove =
							tempCoords.view
							.map(from => play(board, player, from, target, lstOpenCoords))
							.collectFirst { case (Some(nb), no) => (nb, no) }

						maybeMove match {
							case Some((nb, no)) => (Some(nb), nextR.asInstanceOf[MyRandom], no, Some(target))
							case None           => tempFrom(tempOpenCoords.filterNot(_ == target), nextR.asInstanceOf[MyRandom])
						}
					}
				}

				tempFrom(lstOpenCoords, r)
		}
	}
  

	def randomMove(lstOpenCoords: List[Coord2D], rand: MyRandom): (Coord2D, MyRandom) = {
		require(lstOpenCoords.nonEmpty, "lstOpenCoords must not be empty")
		val (rnum, nextR) = rand.nextInt(lstOpenCoords.length)
		(lstOpenCoords(rnum), nextR.asInstanceOf[MyRandom])
	}

	def getCommand(prompt: String, command: String): Any = {
		val result = 
			(if command.nonEmpty then command else Functions.readInput(prompt + ": "))
				.trim
				.toLowerCase
				.split("\\s+")
				.filter(_.nonEmpty)
				.toList

		result match {
			case 
				"undo" :: Nil
				| "quit" :: Nil
				| "change"::Nil
				| "pr"::Nil
				| "pr-single"::Nil
				| "restart"::Nil  => result.head
			case "play" :: colFrom :: rowFrom :: colTo :: rowTo :: Nil =>
				(stringToCoord(colFrom.toString, rowFrom.toString), stringToCoord(colTo.toString, rowTo.toString))
			case _ => getCommand(prompt,command)
		}

	}

	def stringToCoord(s1: String, s2: String): Coord2D = {
		try {
			Coord2D(s1.toInt, s2.toInt)
		} catch {
			case _: NumberFormatException =>
				throw new IllegalArgumentException(s"Not a valid input")
		}
	}

	def getAllPlaysForCoord(state: State, coor:Coord2D): List[Coord2D] = {

		// Baseado no State.hasVictory
		moves(coor).filter {
			c => play(state.board, state.player, coor, c, state.lstOpenCoords)._1.nonEmpty
		}
	}

	def getLongestPathPlay(
		state: State,
		current: Coord2D,
		path: List[(Coord2D, Coord2D)]=Nil
	): List[(Coord2D, Coord2D)] = {

		val nextMoves = {
			moves(current).filter { c =>
				play(state.board, state.player, current, c, state.lstOpenCoords)._1.nonEmpty
			}
		}

		if(nextMoves.isEmpty){ path }
		else {
			val paths = nextMoves.map { next =>
				val (b, o) = play(state.board, state.player, current, next, state.lstOpenCoords)
				b match {
					case Some(nb) =>
						val newState = {
							state.copy(
								board = nb,
								lstOpenCoords = o,
							)
						}
						getLongestPathPlay(
							newState,
							next, 
							path:+ (current, next)
						)
					case None => path
				}
			}

			paths.maxBy(_.size)
		}
	}

	def getLongestPathPlayWithDepth(
		state: State,
		current: Coord2D,
		minPlays:Int,
		pathmin:List[(Coord2D,Coord2D)],
		path: List[(Coord2D, Coord2D)]=Nil
	): (List[(Coord2D, Coord2D)],Int) = {

		val nextMoves = {
		moves(current).filter { c =>
			play(state.board, state.player, current, c, state.lstOpenCoords)._1.nonEmpty
		}
		}

		if(nextMoves.isEmpty){ (pathmin,minPlays) }
		else {
			val paths = nextMoves.map { next =>
				val (b, o) = play(state.board, state.player, current, next, state.lstOpenCoords)
				b match {
				case Some(nb) =>
					val newState = {
					state.copy(
						board = nb,
						lstOpenCoords = o,
					)
					
					}
					val oponentPlays=countOpponentMoves(newState)
					val (newMin, newPathMin) =
					if (oponentPlays < minPlays)
						(oponentPlays, path :+ (current, next))
					else
						(minPlays, pathmin)
					getLongestPathPlayWithDepth(
						newState,
						next,
						newMin,
						newPathMin,
						path:+ (current, next)
					)
				case None => (pathmin,minPlays)
				}
			}
			paths.minBy(_._2)
		}
	}

	def countOpponentMoves(state: State): Int = {
		state.lstOpenCoords.map { c =>
			moves(c).count { next =>
			play(
				state.board,
				state.player,
				next,
				c,
				state.lstOpenCoords
			)._1.nonEmpty
			}

		}.sum
	}

	def moves(coor: Coord2D): List[Coord2D] = List(Coord2D(coor.x+2, coor.y), Coord2D(coor.x-2, coor.y), Coord2D(coor.x, coor.y+2), Coord2D(coor.x, coor.y-2))

	def canContinue(state: State, from: Coord2D): State = {
		if moves(from).exists(to => play(state.board, state.player, from, to, state.lstOpenCoords)._1.nonEmpty)
		then
			State(
				state.board, state.player,
				state.lstOpenCoords,
				state.turn, state.rand,
				state.startTime, state.duration,
				state.dimensions, Some(state),
				state.score, Some(from), state.botStone,
				state.difficulty
			)
		else
			State(
				state.board, Engine.oppositeStone(state.player),
				state.lstOpenCoords, state.turn+1,
				state.rand, Functions.getMillis(),
				state.duration, state.dimensions,
				Some(state), state.score, None,
				state.botStone, state.difficulty
			)
	}

	/*def getSingleJumps(state: State, from:Coord2D): List[Coord2D] = {
		def hasContinuation(board: Board, open:List[Coord2D], at: Coord2D): Boolean =
			moves(at) match {
				case Nil => false
				case to::remainder =>
					play(board, state.player, at, to, open)._1.nonEmpty || hasContinuationInList(board, open, at, remainder)
			}
		def hasContinuationInList(board: Board, open: List[Coord2D], at: Coord2D, lst:List[Coord2D]): Boolean =
			lst match {
				case Nil => false
				case to :: remainder =>
					play(board, state.player, at, to, open)._1.nonEmpty || hasContinuationInList(board, open, at, remainder)
			}

		
		def recursion(lst: List[Coord2D]): List[Coord2D] =
			lst match {
				case Nil => Nil
				case to :: remainder =>
					val continuation = recursion(remainder)
					play(state.board, state.player, from, to, state.lstOpenCoords) match {
						case (Some(newBoard), newOpen) if !hasContinuation(newBoard, newOpen, to) => to :: continuation
						case _ => continuation
					}
			}

		recursion(moves(from))
	}*/

	def getNextState(state: State, comm: Any): State = {
		comm match {
			case (coordFrom:Coord2D, coordTo:Coord2D) =>
				if state.coordPos.contains(coordFrom) || state.coordPos.isEmpty then
					val (newBoard, newOpen) = Engine.play(
					state.board,
					state.player,
					coordFrom,
					coordTo,
					state.lstOpenCoords
					)
					val scorer = state.player match {
						case Stone.Black => Score(state.score.black + 1, state.score.white)
						case Stone.White => Score(state.score.black, state.score.white+1)
					}

					
					newBoard match {
						case Some(newBoard) =>
							val scorer = state.player match {
							case Stone.Black => Score(state.score.black + 1, state.score.white)
							case Stone.White => Score(state.score.black, state.score.white + 1)
							}

							val movedState = State(
								newBoard, state.player, newOpen,
								state.turn, state.rand, state.startTime,
								state.duration, state.dimensions,
								Some(state), scorer, Some(coordTo),
								state.botStone, state.difficulty
							)
							
							val s = canContinue(movedState, coordTo) 
							Functions.writeInputAppend("state.txt", Functions.getStateToString(s))
							s
						
						case None =>
							Functions.output(Console.RED + "Invalid move" + Console.RESET)
							state
					}
				else
					Functions.output(Console.RED + "Invalid Play" + Console.RESET)
					state


			case "pr" => 
				val scorer = state.player match {
					case Stone.Black => Score(state.score.black + 1, state.score.white)
					case Stone.White => Score(state.score.black, state.score.white + 1)
				}

				state.difficulty match {
					case Difficulty.Easy | Difficulty.Medium => 			
						val (newBoard, newRand, newOpen, newPos) =
							Engine.playRandomly(
								state.board,
								state.rand,
								state.player,
								state.lstOpenCoords,
								state.coordPos,
								Engine.randomMove
							)
						
						(newBoard, newPos) match {
							case (Some(nb), Some(np)) =>
								Functions.output("Random move: " + np)
								val movedState = State(
									nb, state.player, newOpen,
									state.turn, newRand, state.startTime,
									state.duration, state.dimensions,
									Some(state), scorer, Some(np), state.botStone,
									state.difficulty
								)
								val s = canContinue(movedState, np)
								Functions.writeInputAppend("state.txt", Functions.getStateToString(s))
								s

							case (Some(nb), None) =>
								Functions.output("Can't Continue")
								val s = State(
									nb, Engine.oppositeStone(state.player),
									newOpen, state.turn+1, newRand,
									Functions.getMillis(), state.duration, state.dimensions,
									Some(state), state.score, None, state.botStone,
									state.difficulty
								)
								Functions.writeInputAppend("state.txt", Functions.getStateToString(s))
								s
							
							case (None, _) =>
								Functions.output("Can't Continue")
								val s = State(
									state.board, Engine.oppositeStone(state.player),
									state.lstOpenCoords, state.turn + 1,
									newRand, Functions.getMillis(), state.duration,
									state.dimensions, Some(state), state.score, 
									None, state.botStone, state.difficulty
								)
								Functions.writeInputAppend("state.txt", Functions.getStateToString(s))
								s
										
						}
					case Difficulty.Hard => 
						val src: List[Coord2D] =
							state.coordPos match {
								case Some(forced) => List(forced)
								case None =>
									state.board.collect { 
										case (c,s) if s == state.player => c
									}.toList
							}
						if (src.isEmpty) {
							val s = State(
								state.board, Engine.oppositeStone(state.player),
								state.lstOpenCoords, state.turn + 1,
								state.rand, Functions.getMillis(), state.duration,
								state.dimensions, Some(state), state.score,
								None, state.botStone, state.difficulty
							)
							Functions.writeInputAppend("state.txt", Functions.getStateToString(s))
							s
						} else {
							val paths = src.map(from => getLongestPathPlay(state, from)).filter(_.nonEmpty)
							if(paths.isEmpty){
								val s = State(
									state.board, oppositeStone(state.player),
									state.lstOpenCoords, state.turn+1,
									state.rand, Functions.getMillis(), state.duration,
									state.dimensions, Some(state), state.score,
									None, state.botStone, state.difficulty
								)
								Functions.writeInputAppend("state.txt", Functions.getStateToString(s))
								s
							} else {
								val bestP = paths.maxBy(_.size)
								val (from, to) = bestP.head

								val (newBoard, newOpen) = 
									play(
										state.board,
										state.player,
										from,
										to,
										state.lstOpenCoords
									)
								
								newBoard match {
									case Some(nb) =>
										val movedState = State(
											nb, state.player, newOpen,
											state.turn, state.rand, state.startTime,
											state.duration, state.dimensions,
											Some(state), scorer, Some(to),
											state.botStone, state.difficulty
										)
										val s = canContinue(movedState,to)
										Functions.writeInputAppend("state.txt", Functions.getStateToString(s))
										s

									case None => state
								}
							}
						}
					case Difficulty.Extreme => 
						val src: List[Coord2D] =
							state.coordPos match {
							case Some(forced) => List(forced)
							case None =>
								state.board.collect {
								case (c, s) if s == state.player => c
								}.toList
							}

						if (src.isEmpty) {
							val s = State(
								state.board, Engine.oppositeStone(state.player),
								state.lstOpenCoords, state.turn + 1,
								state.rand, Functions.getMillis(), state.duration,
								state.dimensions, Some(state), state.score,
								None, state.botStone, state.difficulty
							)
							Functions.writeInputAppend("state.txt", Functions.getStateToString(s))
							s
						} else {
							val evaluated: List[(List[(Coord2D, Coord2D)], Int)] =
							src.map(from => getLongestPathPlayWithDepth(state, from, Int.MaxValue, Nil))

							val nonEmpty = evaluated.filter(_._1.nonEmpty)

							if (nonEmpty.isEmpty) {
								val s = State(
									state.board, Engine.oppositeStone(state.player),
									state.lstOpenCoords, state.turn + 1,
									state.rand, Functions.getMillis(), state.duration,
									state.dimensions, Some(state), state.score,
									None, state.botStone, state.difficulty
								)
								Functions.writeInputAppend("state.txt", Functions.getStateToString(s))
								s
							} else {
								val (bestPath, _) = nonEmpty.minBy(_._2)
								val (from, to) = bestPath.head

								val (newBoard, newOpen) =
									play(state.board, state.player, from, to, state.lstOpenCoords)

								newBoard match {
									case Some(nb) =>
									val movedState = State(
										nb, state.player, newOpen,
										state.turn, state.rand, state.startTime,
										state.duration, state.dimensions,
										Some(state), scorer, Some(to),
										state.botStone, state.difficulty
									)
									val s = canContinue(movedState, to)
									Functions.writeInputAppend("state.txt", Functions.getStateToString(s))
									s

									case None =>
									state
								}
							}
						}
				}
			/*case "pr-single" => 
				if (state.player != state.botStone) state
				else {
					val scorer = state.player match {
						case Stone.Black => Score(state.score.black + 1, state.score.white)
						case Stone.White => Score(state.score.black, state.score.white + 1)
					}

					val src = //source de peças moviveis
						state.coordPos match {
							case Some(forced) => List(forced)
							case None => state.board.collect {
								case (c,s) if s == state.botStone => c 
							}.toList
						}

					def buildLst(src: List[Coord2D], isSingle: Boolean): List[(Coord2D, Coord2D)] =
						src match {
							case Nil => Nil
							case from::remainder =>
								val target =
									if(isSingle) getSingleJumps(state, from)
									else getAllPlaysForCoord(state, from)
								target.map(to => (from, to)) ++ buildLst(remainder, isSingle)
						}

					val singleMoves = buildLst(src, true)
					val targets =
						if (singleMoves.nonEmpty) singleMoves
						else buildLst(src, false)


					if (targets.isEmpty) {
						val containsBotMoves =
							src.exists(from => getAllPlaysForCoord(state, from).nonEmpty)

						if (containsBotMoves) {
							getNextState(state, "pr")
						} else {
							State(
								state.board, oppositeStone(state.player),
								state.lstOpenCoords, state.turn+1,
								state.rand, Main.getMillis(), state.duration,
								state.dimensions, Some(state), state.score,
								None, state.botStone, state.difficulty
							)
						}
					} else {
						val(f, r) = state.rand.nextInt(targets.length)
						val nextRand = r.asInstanceOf[MyRandom]
						val (from, to) = targets(f)

						val (potentialBoard, newOpen) = play(state.board, state.player, from, to, state.lstOpenCoords)

						potentialBoard match {
							case Some(nb) =>
								println(s"[pr-single] chosen from=$from to=$to -> SUCCESS")
								if (singleMoves.nonEmpty) {
									State(
										nb, oppositeStone(state.player),
										newOpen, state.turn+1,
										nextRand, Main.getMillis(), state.duration,
										state.dimensions, Some(state), state.score,
										None, state.botStone, state.difficulty
									)
								} else {
									val movedState = State(
										nb, state.player, newOpen,
										state.turn, nextRand, state.startTime,
										state.duration, state.dimensions,
										Some(state), scorer, Some(to),
										state.botStone,state.difficulty
									)
									canContinue(movedState, to)
								}
							case None => 
								println(s"[pr-single] chosen from=$from to=$to -> FAILED play(None)")
								State(
									state.board, oppositeStone(state.player),
									state.lstOpenCoords, state.turn + 1,
									nextRand, Main.getMillis(), state.duration,
									state.dimensions, Some(state), state.score,
									None, state.botStone, state.difficulty
								)
						}
					}
				}
			*/

			case "quit" => 
				Functions.writeInputAppend("state.txt", Functions.getStateToString(state))
				Functions.doQuit() 
				state


			case "restart" =>
				// Restart volta ao inicio original do jogo
				// através de uma recursão
				def getFirstState(state: State): State = {
					if state.oldState == None then
						val s = State(
							state.board, state.player, state.lstOpenCoords,
							state.turn, state.rand, Functions.getMillis(),
							state.duration, state.dimensions, state.oldState,
							state.score, state.coordPos,
							state.botStone, state.difficulty
						)
						Functions.writeInputAppend("state.txt", Functions.getStateToString(s))
						s
					else
						getFirstState(state.oldState.getOrElse(state))
				}
				getFirstState(state)


			case "change" =>
				val s = State(
					state.board, Engine.oppositeStone(state.player),
					state.lstOpenCoords, state.turn+1, state.rand,
					Functions.getMillis(), state.duration, state.dimensions,
					Some(state), state.score, None, state.botStone,
					state.difficulty
				)
				Functions.writeInputAppend("state.txt", Functions.getStateToString(s))
				s


			case "undo" => 
				val s = state.oldState.getOrElse(state) // Validação para um possível erro de tipo
				Functions.writeInputAppend("state.txt", Functions.getStateToString(s))
				s
			case None => 
				Functions.output(Console.RED + "Invalid command" + Console.RESET)
				state
			case _ => 
				Functions.output(Console.RED + "Not a known command" + Console.RESET)
				state
			
		}
	}

}
