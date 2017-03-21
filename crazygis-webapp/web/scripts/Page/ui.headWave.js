; (function ($) {
	//波浪特效
	var Wave = function (canvas, option) {
		//x轴 x坐标的数量，单位百
		this.K = 2;
		//常数
		this.F = 6;

		this.speed = option.speed || 0.01;
		//this.peak = option.noise || 0;
		this.phase = option.phase || 0;

		this.width = option.width || canvas.width();
		this.height = option.height || canvas.height();

		this.MAX = this.height;

		this.peak = 0.25 * this.MAX;

		this.canvas = canvas.get(0);
		this.canvas.width = this.width;
		this.canvas.height = this.height;
		this.context = this.canvas.getContext('2d');

		this.run = false;
	};
	Wave.prototype = {
		_globalAttenuationFn: function (x, k) {
			return Math.pow(k * 4 / (k * 4 + Math.pow(x, 4)), k);
		},
		_drawLine: function (attenuation, color, width, k, f) {
			this.context.moveTo(0, 0);
			this.context.beginPath();
			this.context.strokeStyle = color;
			this.context.lineWidth = width || 1;
			var x, y;
			this.context.lineTo(0, this.height);
			for (var i = -k; i <= k; i += 0.01) {
				x = this.width * ((i + k) / (k * 2));
				y = this.height / 2 + this.peak * this._globalAttenuationFn(i, k) * (1 / attenuation) * (Math.sin(f * i * .2 - this.phase));
				this.context.lineTo(x, y);
			}
			this.context.lineTo(this.width, this.height);

			this.context.stroke();
			this._fillStyle();

		},
		_fillStyle: function () {
			var gradient = this.context.createLinearGradient(0, 0, 0, this.height);
			gradient.addColorStop(0, "rgba(173, 212, 234, 0)");
			gradient.addColorStop(1, "rgba(12, 172, 188, 1)");
			this.context.fillStyle = gradient;
			this.context.fill();

			//this.context.fillStyle = "rgba(15, 128, 193, .5)";
			//this.context.fill();
		},
		_clear: function () {
			this.context.globalCompositeOperation = 'destination-out';
			this.context.fillRect(0, 0, this.width, this.height);
			this.context.globalCompositeOperation = 'source-over';
		},
		_draw: function () {
			if (!this.run) return;

			this.phase = (this.phase + this.speed) % (Math.PI * 64);
			this._clear();
			this._drawLine(1, 'rgba(173, 212, 234, 0.6)', 1.5, this.K, this.F);
			this._drawLine(0.8, 'rgba(173, 212, 234, 1)', 2, this.K * 1.15, this.F * 1.2);

			var that = this,
                callee = arguments.callee;
			requestAnimationFrame(function () {
				callee.call(that);
			}, 1000);
		},

		start: function () {
			this.phase = 0;
			this.run = true;
			this._draw();
		},

		stop: function () {
			this.run = false;
			this._clear();
		}
	};

	var effects = {
		wave: function (canvas, option) {
			var wave = null;
			if (!!canvas) {
				if (!this.core.isJQueryObject(canvas)) {
					canvas = $(canvas);
				}
				if (!canvas.get(0).getContext) {
					return wave;
				}
				wave = new Wave(canvas, option);
			}
			return wave;
		}
	};

	ui.createEffects = function (canvas, type, option) {
		option = $.extend({}, option);
		if (effects.hasOwnProperty(type)) {
			return effects[type].call(ui, canvas, option);
		} else {
			return null;
		}
	};

})(jQuery);