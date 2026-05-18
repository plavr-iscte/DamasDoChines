package code.code

final case class Session(state: Option[State]) {
    def setState(s: State): Session = copy(state = Some(s))
    def getState: Option[State] = state
}
