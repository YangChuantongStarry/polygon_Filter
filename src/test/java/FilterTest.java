
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class FilterTest {


    public static void main(String[] args) throws IOException {


        Web3j web3j = Web3j.build(new HttpService("https://go.getblock.io/************"));

        BigInteger start = new BigInteger("35887272");
        BigInteger end = new BigInteger("35887277");
        //nft transfer topic
        String topic = "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef";
        List<String> list = new ArrayList<>();
        EthFilter filter = new EthFilter(
                DefaultBlockParameter.valueOf(start),
                DefaultBlockParameter.valueOf(end),
                list);

        filter.addSingleTopic(topic);
        EthLog ethLog1 = getEthLog(web3j, filter);
        EthLog ethLog2 = getEthLog(web3j, filter);
        System.out.println(ethLog1.getLogs().size());
        System.out.println(ethLog2.getLogs().size());
    }

    public static EthLog getEthLog(Web3j web3j, EthFilter filter) throws IOException {
        org.web3j.protocol.core.methods.response.EthFilter ethFilter = web3j.ethNewFilter(filter).send();
        BigInteger filterId = ethFilter.getFilterId();
        System.out.println(filterId);
        EthLog ethLog = web3j.ethGetFilterLogs(filterId).send();
        while (ethLog.hasError()) {
            ethLog = web3j.ethGetFilterLogs(filterId).send();
        }
        return ethLog;
    }
}
