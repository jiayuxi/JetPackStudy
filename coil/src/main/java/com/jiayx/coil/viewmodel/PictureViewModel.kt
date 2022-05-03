package com.jiayx.coil.viewmodel

import androidx.lifecycle.ViewModel
import com.jiayx.coil.bean.Pictures

/**
 *Created by yuxi_
on 2022/5/3
 */
class PictureViewModel : ViewModel() {
    private val urls = arrayListOf(
        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fnimg.ws.126.net%2F%3Furl%3Dhttp%253A%252F%252Fdingyue.ws.126.net%252F2022%252F0208%252F59a31419j00r6z24x001uc000hs00z4c.jpg%26thumbnail%3D650x2147483647%26quality%3D80%26type%3Djpg&refer=http%3A%2F%2Fnimg.ws.126.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1654157904&t=d0ed9d0fe7aec9c32340b9a6c481362f",
        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fpic%2Fb%2Fc3%2F14231116369.jpg&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1654158100&t=a815dd183e11618dc498125239ac9aa2",
        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg5.51tietu.net%2Fpic%2F2019-082411%2Fvoih2iu5uxkvoih2iu5uxk.jpg&refer=http%3A%2F%2Fimg5.51tietu.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1654158205&t=3b646eb9e8c7b6bd4efdf243aa13bf4f",
        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg5.51tietu.net%2Fpic%2F2019-082005%2Fnh4sg1ggvyqnh4sg1ggvyq.jpg&refer=http%3A%2F%2Fimg5.51tietu.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1654158241&t=e3d60d81ac5ada9717efe47778343d37",
        "https://pics1.baidu.com/feed/58ee3d6d55fbb2fbb1b725ffe79674a34423dca7.png?token=32d286acf6b21eead54bc9aa47e445a0",
        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.lemeitu.com%2Fm00%2F8b%2F3e%2F0cc04c9a2b4fa8da0bea4c77f089831c__w.jpg&refer=http%3A%2F%2Fimg.lemeitu.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1654158332&t=c0fd0781e2e22c1efc1780c5de886516",
        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fpic%2F2%2Ff6%2F3c151252383.jpg%3Fdown&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1654158346&t=b90097bf7c62be15a22b25f6025a23b5",
        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fpic%2Ff%2F5d%2F35eb1482225.jpg%3Fdown&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1654158409&t=8e8880e4484a395919ad6a3cd852e078",
        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fwww.2008php.com%2F09_Website_appreciate%2F10-07-11%2F1278861720_g.jpg&refer=http%3A%2F%2Fwww.2008php.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1654010430&t=e5cdc712f1ab52eba269bee9fbeae0e1",
        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg5.51tietu.net%2Fpic%2F2019-082106%2F1xozklyuhp01xozklyuhp0.jpg&refer=http%3A%2F%2Fimg5.51tietu.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1654158484&t=73a2ac656301585ba8bc29fa77309af3",
        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fnimg.ws.126.net%2F%3Furl%3Dhttp%253A%252F%252Fdingyue.ws.126.net%252F2021%252F1120%252Fab5f5787j00r2vdqs000ud000g000ksp.jpg%26thumbnail%3D650x2147483647%26quality%3D80%26type%3Djpg&refer=http%3A%2F%2Fnimg.ws.126.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1654158518&t=8287546720669f5de64ec5bd7190eab2",
        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2Faea02e2bf87ec19b386862baf5b6f0b2a1fa358161fed-10PDle_fw658&refer=http%3A%2F%2Fhbimg.b0.upaiyun.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1654158548&t=aa4f18eaccb94546785fac3408b3ea50"
    )
    val arrayList by lazy {
        val temp = arrayListOf<Pictures>()
        urls.forEachIndexed { index, string ->
            temp.add(Pictures("picture $index", string))
        }
        return@lazy temp
    }
}