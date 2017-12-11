// take the average of all the .json files, passed in as CLI arguments

let objs = [];
let count = 0;

process.argv.forEach((val, idx) => {
	// skip node.exe and the script name
	if (idx > 1) {
		count++;
		objs.push(require(val));
	}
});

let fin = {
	end : {
		sum_sent : {
			start : 0,
			end : 0,
			seconds : 0,
			bytes : 0,
			bits_per_second : 0,
			retransmits : 0
		},
		sum_received : {
			start : 0,
			end : 0,
			seconds : 0,
			bytes : 0,
			bits_per_second : 0
		},
		cpu_utilization_percent: {
			host_total: 0,
			host_user: 0,
			host_system: 0,
			host_user_normalized: 0,
			host_system_normalized: 0,
			remote_total: 0,
			remote_user: 0,
			remote_system: 0,
			remote_user_normalized: 0,
			remote_system_normalized: 0
		}
	}
};

objs.forEach((val) => {
	fin.end.sum_sent.start += val.end.sum_sent.start;
	fin.end.sum_sent.end += val.end.sum_sent.end;
	fin.end.sum_sent.seconds += val.end.sum_sent.seconds;
	fin.end.sum_sent.bytes += val.end.sum_sent.bytes;
	fin.end.sum_sent.bits_per_second += val.end.sum_sent.bits_per_second;
	fin.end.sum_sent.retransmits += val.end.sum_sent.retransmits;

	fin.end.sum_received.start += val.end.sum_received.start;
	fin.end.sum_received.end += val.end.sum_received.end;
	fin.end.sum_received.seconds += val.end.sum_received.seconds;
	fin.end.sum_received.bytes += val.end.sum_received.bytes;
	fin.end.sum_received.bits_per_second += val.end.sum_received.bits_per_second;

	fin.end.cpu_utilization_percent.host_total += val.end.cpu_utilization_percent.host_total;
	fin.end.cpu_utilization_percent.host_user += val.end.cpu_utilization_percent.host_user;
	fin.end.cpu_utilization_percent.host_system += val.end.cpu_utilization_percent.host_system;
	fin.end.cpu_utilization_percent.host_user_normalized += val.end.cpu_utilization_percent.host_user / val.end.cpu_utilization_percent.host_total * 100;
	fin.end.cpu_utilization_percent.host_system_normalized += val.end.cpu_utilization_percent.host_system / val.end.cpu_utilization_percent.host_total * 100;

	fin.end.cpu_utilization_percent.remote_total += val.end.cpu_utilization_percent.remote_total;
	fin.end.cpu_utilization_percent.remote_user += val.end.cpu_utilization_percent.remote_user;
	fin.end.cpu_utilization_percent.remote_system += val.end.cpu_utilization_percent.remote_system;
	fin.end.cpu_utilization_percent.remote_user_normalized += val.end.cpu_utilization_percent.remote_user / val.end.cpu_utilization_percent.remote_total * 100;
	fin.end.cpu_utilization_percent.remote_system_normalized += val.end.cpu_utilization_percent.remote_system / val.end.cpu_utilization_percent.remote_total * 100;
});

fin.end.sum_sent.start /= count;
fin.end.sum_sent.end /= count;
fin.end.sum_sent.seconds /= count;
fin.end.sum_sent.bytes /= count;
fin.end.sum_sent.bits_per_second /= count;
fin.end.sum_sent.retransmits /= count;

fin.end.sum_received.start /= count;
fin.end.sum_received.end /= count;
fin.end.sum_received.seconds /= count;
fin.end.sum_received.bytes /= count;
fin.end.sum_received.bits_per_second /= count;

fin.end.cpu_utilization_percent.host_total /= count;
fin.end.cpu_utilization_percent.host_user /= count;
fin.end.cpu_utilization_percent.host_system /= count;
fin.end.cpu_utilization_percent.host_user_normalized /= count;
fin.end.cpu_utilization_percent.host_system_normalized /= count;

fin.end.cpu_utilization_percent.remote_total /= count;
fin.end.cpu_utilization_percent.remote_user /= count;
fin.end.cpu_utilization_percent.remote_system /= count;
fin.end.cpu_utilization_percent.remote_user_normalized /= count;
fin.end.cpu_utilization_percent.remote_system_normalized /= count;

console.log(JSON.stringify(fin, null, '\t'));
