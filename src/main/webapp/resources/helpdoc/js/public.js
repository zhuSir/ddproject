$(function() {
	var u = navigator.userAgent;
	app = navigator.appVersion;
	var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1; // android终端
	var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); // ios终端
	// alert('是否是Android：'+isAndroid);
	// alert('是否是iOS：'+isiOS);
	if (isAndroid) {
		$('.content .p1 a').click(function() {
			window.location.href = 'dweibao://suggestion';
		});
	}
	if (isiOS) {
		$('.content .p1 a').click(function() {
			toHelpDoc();
		});
	}
})
