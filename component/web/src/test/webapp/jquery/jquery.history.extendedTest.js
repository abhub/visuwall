define(['curl/tdd/runner', 'require'], function (runner, require) {
	runner(require).run(function(require) {

require(['js!Underscore.js!order',
        'js!jquery-1.7.2.js!order',
        'js!jquery/jquery.history.js!order',
        'js!jquery/jquery.history.extended.js!order'], function() {
	
	describe('Query builder test', function() {
		
		it('should remove forced when forcing on already forced url aka: toggle force', function() {
			
			var url = $.history.queryBuilder('wall!/toto42').forceController('wall',  'toto42').build();

			expect(url).toBe("wall/toto42");
		});
		
		it('should create url for simple controller', function() {

			var url = $.history.queryBuilder().addController('wall',  'toto42').build();
			
			expect(url).toBe('wall/toto42');
		});

		it('should add ! when force controller', function() {

			var url = $.history.queryBuilder('wall/toto42').forceController('wall',  'toto42').build();

			expect(url).toBe("wall!/toto42");
		});
		
		it('should parse simple controller request', function() {
			
			var parseRequest = $.history.parseRequest('wall/toto42');
			
			expect(parseRequest).toEqual([{ctrlVars : ['toto42'], ctrl : 'wall', vars : {}, forced : false}]);
		});

		it('should parse forced controller request', function() {
			
			var parseRequest = $.history.parseRequest('wall!/toto42');
			
			expect(parseRequest).toEqual([{ctrlVars : ['toto42'], ctrl : 'wall', vars : {}, forced : true}]);
		});
			
	});
});


	}).then(loaded);
});
