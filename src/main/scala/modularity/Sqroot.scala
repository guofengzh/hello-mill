package modularity

trait Sqroot {
  def sqroot (x: Float): Float = 
      var y: Float = x / 2 
      var step: Float = x/4
      for a <- 1 to 10 do
          if (y*y)<x then 
             y=y+step
          else 
             y=y-step 
      step = step/2
      y
}
