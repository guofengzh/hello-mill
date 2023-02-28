/**
 *  https://dev.to/markehammons/a-quick-explanation-of-path-dependent-types-4pki
 */
package deps

trait time_t_proto:
  type time_t
  val time_t_integral: Integral[time_t]
  implicit class time_t_ops(time_t: time_t) 
    extends time_t_integral.IntegralOps(time_t)
  implicit class time_t_ord(time_t: time_t) 
    extends time_t_integral.OrderingOps(time_t)

trait time_t_impl[U](using val time_t_integral: Integral[U]) 
  extends time_t_proto:
  type time_t = U

trait Platform extends time_t_proto

object PlatformI386 extends Platform, time_t_impl[Int]
object PlatformX64 extends Platform, time_t_impl[Long]

enum Arch:
    case i386 
    case x86_64

import Arch.* 

val arch = i386

val platform: Platform = if arch == i386 then PlatformI386 
else if arch == x86_64 then PlatformX64
else ???

